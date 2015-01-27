package engine;

import engine.sorting.Sorter;
import engine.sorting.Sorting;
import org.junit.Test;

import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class SorterTest {

    @Test
    public void testSort() throws Exception {
        Sorter<Person> sorter = new Sorter<>();

        Person alice = new Person("Alice", 18);
        alice.setSortElement("name");

        Person bob = new Person("Bob", 17);
        bob.setSortElement("name");

        Person carl = new Person("Carl", 19);
        carl.setSortElement("name");

        sorter.add(bob);
        sorter.add(alice);
        sorter.add(carl);

        sorter.sort(Sorter.ASC);

        assertEquals("Alice", sorter.get(0).name);
        assertEquals("Bob", sorter.get(1).name);
        assertEquals("Carl", sorter.get(2).name);

        sorter.sort(Sorter.DESC);

        sorter.addAll(sorter.stream().collect(Collectors.toList()));
        assertEquals("Carl", sorter.get(0).name);
        assertEquals("Bob", sorter.get(1).name);
        assertEquals("Alice", sorter.get(2).name);
    }

    @Test
    public void testLittleSort() throws Exception {
        Sorter<Person> persons = new Sorter<>();

        Person alice = new Person("Alice", 18);
        alice.setSortElement("name");
        persons.add(alice);
        persons.sort(Sorter.ASC);
        assertEquals("Alice", persons.get(0).name);

    }

    class Person implements Sorting {
        public String name;
        public Integer age;
        Comparable sortElement = name;

        @Override
        public Comparable getSortElement() {
            return sortElement;
        }

        @Override
        public void setSortElement(String sortingby) {
            if (sortingby.equals("name")) {
                sortElement = name;
            } else {
                sortElement = age;
            }
        }

        public Person(String name, Integer age) {
            this.name = name;
            this.age = age;
        }
    }
}