class Problem {
    public static void main(String[] args) {
        for (int i = 0; i < args.length - 1; i++) {
            System.out.printf("%s=%s\n", args[i], args[i + 1]);
            i++;
        }
    }
}