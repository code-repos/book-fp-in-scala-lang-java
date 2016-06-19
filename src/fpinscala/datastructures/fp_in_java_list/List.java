/**
 * This is the immutable, persistent, singly linked list implementation found on page 136 of
 * Pierre-Yves Saumont's book https://www.manning.com/books/functional-programming-in-java,
 * plus equals/hashcode and some assertions
 */

package fpinscala.datastructures.fp_in_java_list;

public abstract class List<A> {

    public abstract A head();
    public abstract List<A> tail();
    public abstract boolean isEmpty();

    @SuppressWarnings("rawtypes")
    public static final List NIL = new Nil();

    private List() {}

    private static class Nil<A> extends List<A> {

        private Nil() {}

        public A head() {
            throw new IllegalStateException("head called on empty list");
        }

        public List<A> tail() {
            throw new IllegalStateException("tail called on empty list");
        }

        public boolean isEmpty() {
            return true;
        }
    }

    private static class Cons<A> extends List<A> {

        private final A head;
        private final List<A> tail;

        private Cons(A head, List<A> tail) {
            this.head = head;
            this.tail = tail;
        }

        public A head() {
            return head;
        }

        public List<A> tail() {
            return tail;
        }

        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Cons<?> cons = (Cons<?>) o;

            if (head != null ? !head.equals(cons.head) : cons.head != null) return false;
            return tail != null ? tail.equals(cons.tail) : cons.tail == null;

        }

        @Override
        public int hashCode()
        {
            int result = head != null ? head.hashCode() : 0;
            result = 31 * result + (tail != null ? tail.hashCode() : 0);
            return result;
        }
    }

    @SuppressWarnings("unchecked")
    public static <A> List<A> list() {
        return NIL;
    }

    @SafeVarargs
    public static <A> List<A> list(A... a) {
        List<A> n = list();
        for (int i = a.length - 1; i >= 0; i--) {
            n = new Cons<>(a[i], n);
        }
        return n;
    }

    public List<A> cons(A a) {
        return new Cons<>(a, this);
    }

    // will of course overflow the stack for long lists
    public static Integer sum(List<Integer> ints) {
        return ints.isEmpty()
                ? 0
                : ints.head() + sum(ints.tail());
    }

    public static void main(String[] args)
    {
        assert(       NIL == list());
        assert(         3 == list(3).head());
        assert(       NIL == list(3).tail());
        assert(         1 == list(1,2,3).head());
        assert( list(2,3).equals(list(1,2,3).tail()));

        assert(            list(3).equals(NIL.cons(3)));
        assert( Integer.valueOf(3).equals(NIL.cons(3).head()));
        assert(                NIL.equals(NIL.cons(3).tail()));
        assert(        list(1,2,3).equals(NIL.cons(3).cons(2).cons(1)));
        assert( list(1,2,3).head().equals(NIL.cons(3).cons(2).cons(1).head()));
        assert( list(1,2,3).tail().equals(NIL.cons(3).cons(2).cons(1).tail()));

        assert(  0 == sum(NIL));
        assert(  3 == sum(list(3)));
        assert( 15 == sum(list(1,2,3,4,5)));
    }
}