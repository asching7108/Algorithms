public class TestDataStructures {

    public static void main(String[] args) {
        LinkedListStack<Integer> s1 = new LinkedListStack<>();
        s1.push(1);
        s1.pop();

        LinkedListQueue<Integer> q1 = new LinkedListQueue<>();
        q1.enqueue(1);
        q1.dequeue();

        ResizingArrayStack<Integer> s2 = new ResizingArrayStack<>(2);
        for (int i = 0; i < 50; i++) {
            s2.push(i);
        }
        for (Integer i : s2) {
            System.out.print(i + " ");
        }
        System.out.println();

        ResizingArrayQueue<Integer> q2 = new ResizingArrayQueue<>(2);
        for (int i = 0; i < 50; i++) {
            q2.enqueue(i);
        }
        for (Integer i : q2) {
            System.out.print(i + " ");
        }
        System.out.println();

        Integer[] ms = new Integer[]{5, 4, 2, 7, 4, 6, 9, 8, 1, 1, 3, 1, 2};
        MergesortBU.sort(ms);
        for (Integer i : ms) {
            System.out.print(i + " ");
        }
        System.out.println();

        Integer[] msbu = new Integer[]{5, 4, 2, 7, 4, 6, 9, 8, 1, 1, 3, 1, 2};
        MergesortBU.sort(msbu);
        for (Integer i : msbu) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

}
