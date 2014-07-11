package com.github.gg;

import com.github.gg.Dict;
import com.github.gg.OptAssign;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class Opt {

    private final Object app;

    public Opt(Object app) {
        this.app = app;
    }

    public String getString(final LinkedHashMap app) {
        StringBuilder builder = new StringBuilder();
        final LinkedHashMap<String, LinkedHashMap> methods = (LinkedHashMap) app;
        for (Map.Entry<String, LinkedHashMap> method : methods.entrySet()) {
            final String mname = method.getKey();
            builder.append(String.format("void %s(){", mname));
            builder.append("\n");
            final LinkedHashMap<String, ArrayList> labels = method.getValue();
            for (Map.Entry<String, ArrayList> label : labels.entrySet()) {
                final String lname = label.getKey();
                final ArrayList stmts = label.getValue();

                if (!lname.isEmpty()) {
                    builder.append(lname);
                    builder.append(":");
                    builder.append("\n");
                }
                for (int i = 0; i < stmts.size(); i++) {
                    Object stmt = stmts.get(i);
                    builder.append(stmt);
                    builder.append("\n");
                }

            }
            builder.append("}");
            builder.append("\n");
        }
        return builder.toString();
    }

    public LinkedHashMap opt() {
        final LinkedHashMap<String, LinkedHashMap> methods_new = new LinkedHashMap<>();
        final LinkedHashMap<String, LinkedHashMap> methods = (LinkedHashMap) this.app;
        for (Map.Entry<String, LinkedHashMap> method : methods.entrySet()) {
            final String mname = method.getKey();
            final LinkedHashMap<String, ArrayList> labels = method.getValue();
            final LinkedHashMap<String, ArrayList> labels_new = new LinkedHashMap<>();
            for (Map.Entry<String, ArrayList> label : labels.entrySet()) {
                final String lname = label.getKey();
                final ArrayList stmts = label.getValue();

                labels_new.put(lname, optbb(stmts));
            }
            methods_new.put(mname, labels_new);
        }

        return methods_new;
    }

    public ArrayList optbb(final Object bb) {
        final ArrayList bblock = (ArrayList) bb;
        final String kcount = "count";
        final String kbblock = "bblock";

        Dict res;
        int res_count;
        ArrayList res_bblock = bblock;
        int i = 0;

        do {
            //eviar bucle infinito
            if (i == 100) {
                break;
            }
            i++;

            // subexpresiones comunes
            res = optbb_subexpr(res_bblock);
            res_count = res.getInt(kcount);
            res_bblock = res.getArrayList(kbblock);
            System.out.println(String.format("subexpr -> %d", res_count));
            if (res_count > 0) {
                continue;
            }

            // propagacion de copias
            res = optbb_pcopias(res_bblock);
            res_count = res.getInt(kcount);
            res_bblock = res.getArrayList(kbblock);
            System.out.println(String.format("pcopias -> %d", res_count));
            if (res_count > 0) {
                continue;
            }

            //eliminacion codigo muerto 
            res = optbb_codmuerto(res_bblock);
            res_count = res.getInt(kcount);
            res_bblock = res.getArrayList(kbblock);
            System.out.println(String.format("codmuerto -> %d", res_count));
            if (res_count > 0) {
                continue;
            }

            //simplificacion con onperadores identidad
            res = optbb_identidad(res_bblock);
            res_count = res.getInt(kcount);
            res_bblock = res.getArrayList(kbblock);
            System.out.println(String.format("identidad -> %d", res_count));
            if (res_count > 0) {
                continue;
            }

            //simplificacion con onperadores identidad
            res = optbb_itencidad(res_bblock);
            res_count = res.getInt(kcount);
            res_bblock = res.getArrayList(kbblock);
            System.out.println(String.format("intencidad -> %d", res_count));
            if (res_count > 0) {
                continue;
            }

            return res_bblock;
        } while (true);

        return res_bblock;
    }

    public Dict optbb_subexpr(final Object bb) {
        final ArrayList bblock = (ArrayList) bb;
        final ArrayList bblock_new = new ArrayList<>();
        final LinkedHashMap assigns = new LinkedHashMap<>();
        int count = 0;

        for (Object obj : bblock) {
            if (obj instanceof OptAssign) {
                final OptAssign opta = (OptAssign) obj;
                final Object name = opta.name;
                Object val = opta.val;

                if (assigns.containsKey(val.toString())) {
                    val = assigns.get(val.toString());
                    bblock_new.add(new OptAssign(name, new OptExpr(null, val, null)));
                    count++;
                    continue;
                }
                assigns.put(val.toString(), name);
                bblock_new.add(opta);
                continue;
            }

            bblock_new.add(obj);
        }

        return new Dict("bblock", bblock_new, "count", count);
    }

    public Dict optbb_pcopias(final Object bb) {
        final ArrayList bblock = (ArrayList) bb;
        final ArrayList bblock_new = new ArrayList<>();

        final Dict temps = new Dict();
        int count = 0;

        for (Object obj : bblock) {
            if (obj instanceof OptAssign) {
                final OptAssign opta = (OptAssign) obj;
                final Object opta_name = opta.name;

                final OptExpr opta_val = opta.val instanceof OptExpr ? (OptExpr) opta.val : new OptExpr(null, opta.val, null);

                final Object o = opta_val.o;
                Object l = opta_val.l;
                Object r = opta_val.r;

                if (temps.containsKey(l.toString())) {
                    l = temps.get(l.toString());
                    count++;
                }
                if (r != null && temps.containsKey(r.toString())) {
                    r = temps.get(r.toString());
                    count++;
                }

                final OptExpr opte_new = new OptExpr(o, l, r);
                final OptAssign opta_new = new OptAssign(opta_name, opte_new);

                if (opte_new.r == null) {
                    temps.put(opta_new.name.toString(), opte_new.l);
                }

                bblock_new.add(opta_new);
                continue;
            }
            if (obj instanceof OptIf) {
                final OptIf opti = (OptIf) obj;
                final OptExpr opti_expr = (OptExpr) opti.expr;
                final Object opti_gotot = opti.goto_true;
                Object o = opti_expr.o;
                Object l = opti_expr;
                Object r = opti_expr;

                if (temps.containsKey(l.toString())) {
                    l = temps.get(l.toString());
                    count++;
                }
                if (temps.containsKey(r.toString())) {
                    r = temps.get(r.toString());
                    count++;
                }

                final OptIf opti_new = new OptIf(new OptExpr(o, l, r), opti_gotot);

                bblock_new.add(opti_new);
                continue;
            }
            if (obj instanceof OptMemory) {
                final OptMemory optm = (OptMemory) obj;
                final Object optm_memory = optm.memory;
                final Object optm_position = optm.position;

                Object p = optm_position;

                if (temps.containsKey(p.toString())) {
                    p = temps.get(p.toString());
                    count++;
                }

                final OptMemory optm_new = new OptMemory(optm_memory, p);
                bblock_new.add(optm_new);
                continue;
            }

            bblock_new.add(obj);
        }

        return new Dict("bblock", bblock_new, "count", count);
    }

    public Dict optbb_codmuerto(final Object bb) {
        final ArrayList bblock = (ArrayList) bb;
        final ArrayList bblock_new = new ArrayList<>();

        final Dict temps = new Dict();
        int count = 0;
        // llenar posibles 
        for (Object obj : bblock) {
            if (obj instanceof OptAssign) {
                final OptAssign opta = (OptAssign) obj;
                final Object opta_name = opta.name;

                if (!temps.containsKey(opta_name.toString())) {
                    temps.put(opta_name.toString(), 0);
                }
            }
        }

        // contar referencias
        for (Object obj : bblock) {
            if (obj instanceof OptAssign) {
                final OptAssign opta = (OptAssign) obj;
                final Object opta_name = opta.name;
                final OptExpr opta_val = (OptExpr) opta.val;
                final Object opta_val_l = opta_val.l;
                final Object opta_val_r = opta_val.r;

                final String lkey = opta_val_l.toString();
                if (temps.containsKey(lkey)) {
                    temps.put(lkey, temps.getInt(lkey) + 1);
                }

                String rkey = null;
                if (opta_val_r != null && temps.containsKey(rkey = opta_val_r.toString())) {
                    temps.put(rkey, temps.getInt(rkey) + 1);
                }

                //si la asignacion es a memoria usa un temporal
                if (opta_name instanceof OptMemory) {
                    final OptMemory opta_name_memory = (OptMemory) opta_name;
                    final Object opta_name_memory_position = opta_name_memory.position;
                    final String mkey = opta_name_memory_position.toString();
                    if(temps.containsKey(mkey)){
                        temps.put(mkey, temps.getInt(mkey) + 1);
                    }
                }
                continue;
            }
            if (obj instanceof OptIf) {
                final OptIf opti = (OptIf) obj;
                final OptExpr opti_val = (OptExpr) opti.expr;
                final Object opti_val_l = opti_val.l;
                final Object opti_val_r = opti_val.r;

                final String lkey = opti_val_l.toString();
                if (temps.containsKey(lkey)) {
                    temps.put(lkey, temps.getInt(lkey) + 1);
                }
                final String rkey = opti_val_r.toString();
                if (temps.containsKey(rkey)) {
                    temps.put(rkey, temps.getInt(rkey) + 1);
                }

                continue;
            }
            if (obj instanceof OptMemory) {
                final OptMemory optm = (OptMemory) obj;
                final Object optm_position = optm.position;

                final String key = optm_position.toString();

                if (temps.containsKey(key)) {
                    temps.put(key, temps.getInt(key) + 1);
                }

                continue;
            }
        }

        // eliminar codigo muerto...
        for (Object obj : bblock) {
            if (obj instanceof OptAssign) {
                final OptAssign opta = (OptAssign) obj;
                final Object opta_name = opta.name;

                if (opta_name instanceof OptMemory) {
                    bblock_new.add(obj);
                    continue;
                }
                final String key = opta_name.toString();
                final int refcount = temps.getInt(key);
                if (refcount == 0) {

                    continue;
                }
            }
            bblock_new.add(obj);
        }

        return new Dict("bblock", bblock_new, "count", count);
    }

    public Dict optbb_identidad(final Object bb) {
        final ArrayList bblock = (ArrayList) bb;
        final ArrayList bblock_new = new ArrayList<>();

        final Dict temps = new Dict();
        int count = 0;

        for (Object obj : bblock) {
            if (obj instanceof OptAssign) {
                final OptAssign opta = (OptAssign) obj;
                final Object opta_name = opta.name;
                final OptExpr opta_val = (OptExpr) opta.val;

                final Object l = identReduce(opta_val);
                if (l != null) {
                    bblock_new.add(new OptAssign(opta_name, new OptExpr(null, l, null)));
                    count++;
                    continue;
                }

            }
            bblock_new.add(obj);
        }
        return new Dict("bblock", bblock_new, "count", count);
    }

    public Dict optbb_itencidad(final Object bb) {
        final ArrayList bblock = (ArrayList) bb;
        final ArrayList bblock_new = new ArrayList<>();

        final Dict temps = new Dict();
        int count = 0;

        for (Object obj : bblock) {
            if (obj instanceof OptAssign) {
                final OptAssign opta = (OptAssign) obj;
                final Object opta_name = opta.name;
                final OptExpr opta_val = (OptExpr) opta.val;

                final Object val = itenReduce(opta_val);
                if (val != null) {
                    bblock_new.add(new OptAssign(opta_name, val));
                    count++;
                    continue;
                }
            }
            bblock_new.add(obj);
        }

        return new Dict("bblock", bblock_new, "count", count);
    }

    public Object identReduce(final Object expr) {
        final OptExpr opte = (OptExpr) expr;
        final Object opte_o = opte.o;
        final Object opte_l = opte.l;
        final Object opte_r = opte.r;

        if (opte_r == null) {
            return null;
        }

        final String opte_o_str = opte_o.toString();
        final Double l_num = getDouble(opte_l);
        final Double r_num = getDouble(opte_r);

        if (opte_o_str.equals("+") || opte_o_str.equals("-")) {
            if (l_num != null && l_num == 0) {
                return opte_r;
            }
            if (r_num != null && r_num == 0) {
                return opte_l;
            }
            return null;
        }

        if (opte_o_str.equals("*")) {
            if (l_num != null && l_num == 1) {
                return opte_r;
            }
            if (r_num != null && r_num == 1) {
                return opte_l;
            }
            return null;
        }

        if (opte_o_str.equals("/")) {
            if (r_num != null && r_num == 1) {
                return opte_l;
            }
            return null;
        }

        return null;
    }

    public Double getDouble(final Object val) {
        final String val_str = val.toString();

        try {
            return Double.parseDouble(val_str);
        } catch (NumberFormatException exc) {

        }

        return null;
    }

    public Object itenReduce(final Object expr) {
        final OptExpr opte = (OptExpr) expr;
        final Object opte_o = opte.o;
        final Object opte_l = opte.l;
        final Object opte_r = opte.r;

        if (opte_r == null) {
            return null;
        }

        final String opte_o_str = opte_o.toString();
        final Double l_num = getDouble(opte_l);
        final Double r_num = getDouble(opte_r);

        if (opte_o_str.equals("*")) {
            if (l_num != null && l_num == 2) {
                return new OptExpr(opte_o, opte_r, opte_r);
            }
            if (r_num != null && r_num == 2) {
                return new OptExpr(opte_o, opte_l, opte_l);
            }
            return null;
        }

        return null;
    }

    public Object optbb_consts(final Object bb) {
        final ArrayList bblock = (ArrayList) bb;
        final ArrayList bblock_new = new ArrayList<>();

        final Dict temps = new Dict();
        int count = 0;

        for (Object obj : bblock) {
            if (obj instanceof OptAssign) {
                final OptAssign opta = (OptAssign) obj;
                final Object opta_name = opta.name;
                final OptExpr opta_val = (OptExpr) opta.val;

                final Object o = opta_val.o;
                Object l = opta_val.l;
                Object r = opta_val.r;

                if (temps.containsKey(l.toString())) {
                    l = temps.get(l.toString());
                    count++;
                }
                if (r != null && temps.containsKey(r.toString())) {
                    r = temps.get(r.toString());
                    count++;
                }

                final OptExpr opte_new = new OptExpr(o, l, r);
                final OptAssign opta_new = new OptAssign(opta_name, opte_new);

                if (opte_new.r == null) {
                    temps.put(opta_new.name.toString(), opte_new.l);
                }

                bblock_new.add(opta_new);
                continue;
            }
            if (obj instanceof OptIf) {
                final OptIf opti = (OptIf) obj;
                final OptExpr opti_expr = (OptExpr) opti.expr;
                final Object opti_gotot = opti.goto_true;
                Object o = opti_expr.o;
                Object l = opti_expr;
                Object r = opti_expr;

                if (temps.containsKey(l.toString())) {
                    l = temps.get(l.toString());
                    count++;
                }
                if (temps.containsKey(r.toString())) {
                    r = temps.get(r.toString());
                    count++;
                }

                final OptIf opti_new = new OptIf(new OptExpr(o, l, r), opti_gotot);

                bblock_new.add(opti_new);
                continue;
            }
            if (obj instanceof OptMemory) {
                final OptMemory optm = (OptMemory) obj;
                final Object optm_memory = optm.memory;
                final Object optm_position = optm.position;

                Object p = optm_position;

                if (temps.containsKey(p.toString())) {
                    p = temps.get(p.toString());
                    count++;
                }

                final OptMemory optm_new = new OptMemory(optm_memory, p);
                bblock_new.add(optm_new);
                continue;
            }

            bblock_new.add(obj);
        }

        return new Dict("bblock", bblock_new, "count", count);
    }
    
    public LinkedHashMap BloqueGlobal() {
        final LinkedHashMap<String, LinkedHashMap> methods_new = new LinkedHashMap<>();
        final LinkedHashMap<String, LinkedHashMap> methods = (LinkedHashMap) this.app;
        for (Map.Entry<String, LinkedHashMap> method : methods.entrySet()) {
            final String mname = method.getKey();
            final LinkedHashMap<String, ArrayList> labels = method.getValue();
            final LinkedHashMap<String, ArrayList> labels_new = new LinkedHashMap<>();
            ArrayList Global = new ArrayList();
            for (Map.Entry<String, ArrayList> label : labels.entrySet()) {
                final String lname = label.getKey();
                String tag = "";
                if (!lname.isEmpty()){
                    tag = lname + ":";
                }
                
                final ArrayList stmts = label.getValue();
                Global.add(tag);
                for (int i = 0; i < stmts.size(); i++) {
                    Object stmt = stmts.get(i);
                    Global.add(stmt);
                }
                
            }
            labels_new.put("", optbbG(Global));
            methods_new.put(mname, labels_new);
        }

        return methods_new;
    }
    
    
    public ArrayList optbbG(final Object bb) {
        final ArrayList bblock = (ArrayList) bb;
        final String kcount = "count";
        final String kbblock = "bblock";

        Dict res;
        int res_count;
        ArrayList res_bblock = bblock;
        int i = 0;

        do {
            //eviar bucle infinito
            if (i == 100) {
                break;
            }
            i++;

            // subexpresiones comunes
            res = optbb_subexpr(res_bblock);
            res_count = res.getInt(kcount);
            res_bblock = res.getArrayList(kbblock);
            System.out.println(String.format("subexpr -> %d", res_count));
            if (res_count > 0) {
                continue;
            }

            // propagacion de copias
            res = optbb_pcopias(res_bblock);
            res_count = res.getInt(kcount);
            res_bblock = res.getArrayList(kbblock);
            System.out.println(String.format("pcopias -> %d", res_count));
            if (res_count > 0) {
                continue;
            }
            return res_bblock;
        } while (true);

        return res_bblock;
    }
    
}
