package tacticulous.tira.datastructure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author O
 */
public class TacListTest {

    private TacList<String> list;


    @Test
    public void basicAddWorks() {
        list = new TacList(5);
        list.add("Test1");
        list.add("Test2");
        assertEquals("[Test1, Test2]", list.toString());
        list = new TacList(1000);
        list.add("Test1");
        list.add("Test2");
        assertEquals("[Test1, Test2]", list.toString());
    }

    @Test
    public void listIncreasingAddWorks() {
        list = new TacList(2);
        list.add("Test1");
        list.add("Test2");
        list.add("Test3");
        list.add("Test4");
        assertEquals("[Test1, Test2, Test3, Test4]", list.toString());
    }

    @Test
    public void isEmptyAndClearWork() {
        list = new TacList(2);
        assertTrue(list.isEmpty());
        list.add("Test1");
        list.add("Test2");
        list.add("Test3");
        list.add("Test4");
        assertTrue(!list.isEmpty());
        list.clear();
        assertTrue(list.isEmpty());
        assertEquals("[]", list.toString());
    }

    @Test
    public void iteratorWorks() {
        list = new TacList(4);
        list.add("T1");
        list.add("T2");
        list.add("T3");
        list.add("T4");
        String test = "";
        for (String st : list) {
            test = test + st;
        }
        assertEquals("T1T2T3T4", test);
    }

    @Test
    public void sizeWorks() {
        list = new TacList(2);
        list.add("Test1");
        list.add("Test2");
        list.add("Test3");
        assertEquals(3, list.size());
    }

    @Test
    public void removeWorks1() {
        list = new TacList(2);
        list.add("Test1");
        list.add("Test2");
        list.add("Test3");
        list.add("Test4");
        list.remove("Test3");
        assertEquals(3, list.size());
        assertEquals("[Test1, Test2, Test4]", list.toString());
    }

    @Test
    public void sizeWorks1() {
        list = new TacList(2);
        list.add("Test1");
        list.add("Test2");
        list.add("Test3");
        assertEquals(3, list.size());
    }

    @Test
    public void removeWorks2() {
        list = new TacList(2);
        list.add("Test1");
        list.add("Test2");
        list.add("Test3");
        list.add("Test3");
        list.add("Test4");
        list.add("Test3");
        list.remove("Test3");
        assertEquals(5, list.size());
        assertEquals("[Test1, Test2, Test3, Test4, Test3]", list.toString());
        list.remove("Test3");
        assertEquals("[Test1, Test2, Test4, Test3]", list.toString());
    }

    @Test
    public void getWorks() {
        list = new TacList(2);
        list.add("Test1");
        list.add("Test2");
        list.add("Test3");
        list.add("Test4");
        assertEquals("Test3", list.get(2));
        list.remove("Test3");
        assertEquals("Test4", list.get(2));
    }

    @Test
    public void containsWorks() {
        list = new TacList(2);
        assertTrue(!list.contains("Test3"));
        list.add("Test1");
        list.add("Test2");
        list.add("Test3");
        list.add("Test4");
        assertTrue(list.contains("Test3"));
        list.remove("Test3");
        assertTrue(!list.contains("Test3"));
        list.add("Test4");
        assertTrue(list.contains("Test4"));
        list.remove("Test4");
        assertTrue(list.contains("Test4"));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void illegalIndex1() {
        list = new TacList(2);
        list.get(0);
    }

    @Test
    public void smallSizeWorks() {
        list = new TacList(0);
        list.add("Test1");
        assertEquals("Test1", list.get(0));
        list = new TacList(-1);
        list.add("Test1");
        assertEquals("Test1", list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void illegalIndex2() {
        list = new TacList(2);
        list.add("Test1");
        list.add("Test2");
        list.get(2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void illegalIndex3() {
        list = new TacList(2);
        list.get(-1);
    }

    @Test
    public void hugeListWorks() {
        list = new TacList(2);
        for (int i = 0; i < 10000; i++) {
            list.add("Test");
        }
        assertEquals(10000, list.size());
        for (int i = 0; i < 9000; i++) {
            list.remove("Test");
        }
        assertEquals(1000, list.size());
    }
}
