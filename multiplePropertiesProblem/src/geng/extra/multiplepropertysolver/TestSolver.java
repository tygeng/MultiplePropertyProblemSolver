package geng.extra.multiplepropertysolver;

import java.util.ArrayList;

import java.util.HashSet;

import java.util.List;

import java.util.Set;

public class TestSolver {

    /**
     * @param args
     */
    public static void main(String[] args) {
        List<Set<Option>> list = new ArrayList<Set<Option>>(5);
        Set<Option> day = new HashSet<Option>();
        Set<Option> company = new HashSet<Option>();
        Set<Option> employee = new HashSet<Option>();
        Set<Option> content = new HashSet<Option>();
        Set<Option> reason = new HashSet<Option>();
        list.add(day);
        list.add(company);
        list.add(employee);
        list.add(content);
        list.add(reason);

        Option mon = new Option("Monday");
        Option tue = new Option("Tuesday");
        Option wed = new Option("Wednesday");
        Option thu = new Option("Thursday");
        Option fri = new Option("Friday");

        day.add(mon);
        day.add(tue);
        day.add(wed);
        day.add(thu);
        day.add(fri);

        Option swi = new Option("Swift");
        Option all = new Option("Allspeed");
        Option pdq = new Option("PDQ");
        Option fas = new Option("Fast-R");
        Option qwi = new Option("Qwik");

        company.add(swi);
        company.add(all);
        company.add(pdq);
        company.add(fas);
        company.add(qwi);

        Option uma = new Option("Uma");
        Option ter = new Option("Terrance");
        Option zac = new Option("Zack");
        Option vio = new Option("Violet");
        Option sar = new Option("Sarah");

        employee.add(uma);
        employee.add(ter);
        employee.add(zac);
        employee.add(vio);
        employee.add(sar);

        Option mar = new Option("Marble");
        Option cof = new Option("Coffee Mugs");
        Option nai = new Option("Nails Clippers");
        Option rub = new Option("Rubber Boots");
        Option cer = new Option("Ceramic Owl");

        content.add(mar);
        content.add(cof);
        content.add(nai);
        content.add(rub);
        content.add(cer);

        Option pro = new Option("Promotion");
        Option bab = new Option("Baby");
        Option ret = new Option("Retire");
        Option wedg = new Option("Wedding");
        Option sur = new Option("Surgery");

        reason.add(pro);
        reason.add(bab);
        reason.add(ret);
        reason.add(wedg);
        reason.add(sur);

        Question q = new Question(list);
        Question.Entry c;

        try {
            c = q.addEntry();
            c.editProperty(0, fri);
            c.editProperty(3, null, mar);
            c.editProperty(4, null, pro);

            c = q.addEntry();
            c.editProperty(0, null, fri);
            c.editProperty(3, mar);
            c.editProperty(4, pro);

            c = q.addEntry();
            c.editProperty(0, mon);
            c.editProperty(1, swi);
            c.editProperty(3, null, cof);

            c = q.addEntry();
            c.editProperty(3, cof);

            c = q.addEntry();
            c.editProperty(0, wed);
            c.editProperty(3, nai);
            c.editProperty(4, null, bab);

            c = q.addEntry();
            c.editProperty(4, bab);

            c = q.addEntry();
            c.editProperty(1, all);
            c.editProperty(3, null, rub);
            c.editProperty(4, ret);

            c = q.addEntry();
            c.editProperty(3, rub);

            c = q.addEntry();
            c.editProperty(2, uma);
            c.editProperty(3, cof);


            c = q.addEntry();
            c.editProperty(1, null, pdq);
            c.editProperty(2, ter);

            c = q.addEntry();
            c.editProperty(1, pdq);

            c = q.addEntry();
            c.editProperty(0, null, tue);
            c.editProperty(1, fas);
            c.editProperty(3, null, rub);

            c = q.addEntry();
            c.editProperty(3, rub);
            c.editProperty(2, null, zac);

            c = q.addEntry();
            c.editProperty(2, zac);

            c = q.addEntry();
            c.editProperty(0, null, fri);
            c.editProperty(2, vio);
            c.editProperty(4, null, wedg);


            c = q.addEntry();
            c.editProperty(0, null, mon);
            c.editProperty(4, wedg);

            c = q.addEntry();
            c.editProperty(1, qwi);
            c.editProperty(3, null, rub);
            c.editProperty(4, null, bab);

            c = q.addEntry();
            c.editProperty(3, rub);
            c.editProperty(4, null, bab);

            c = q.addEntry();
            c.editProperty(0, null, mon);
            c.editProperty(2, zac);
            c.editProperty(3, null, nai);

            c = q.addEntry();
            c.editProperty(0, fri);
            c.editProperty(3, null, cer);
            c.editProperty(4, null, bab);

            c = q.addEntry();
            c.editProperty(0, null, fri);
            c.editProperty(3, cer);
            c.editProperty(4, null, bab);



            c = q.addEntry();
            c.editProperty(0, thu);
            c = q.addEntry();
            c.editProperty(2, sar);
            c = q.addEntry();
            c.editProperty(4, sur);

            c = q.addEntry();
            c.editProperty(0, tue);

            c = q.addEntry();
            c.editProperty(0, mon);
            c.editProperty(2, vio);
//
            c = q.addEntry();
            c.editProperty(0, tue);
            c.editProperty(4, wedg);



            Solver solver = new Solver(q);
            solver.solve();
            System.out.println(solver.getSolutions());



        }

        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
