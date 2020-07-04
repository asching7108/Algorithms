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
    }

}
