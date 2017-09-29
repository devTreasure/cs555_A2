package A2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestClass {
   public static void main(String[] args) {
      int i = 2;
      System.out.println(i >= 2);

      // m1();
      // m2();
      // m3();
      m4();
   }

   private static void m4() {
      System.out.println("START");
      assert 1 == highestIdLessThanEqualTo(Arrays.asList(1, 1, 7), 3);
      assert 4 == highestIdLessThanEqualTo(Arrays.asList(4, 4, 7), 3);
      assert 1 == highestIdLessThanEqualTo(Arrays.asList(1, 1, 1), 7);
      assert 1 == highestIdLessThanEqualTo(Arrays.asList(7, 1, 1), 0);
      System.out.println("ENDT");
   }

   private static void m3() {
      System.out.println("START");
      List<Integer> list1 = Arrays.asList(21, 28, 28, 28, 4);
      assert 4 == highestIdLessThanEqualTo(list1, 0);
      assert 4 == highestIdLessThanEqualTo(list1, 4);

      assert 21 == highestIdLessThanEqualTo(list1, 10);
      assert 21 == highestIdLessThanEqualTo(list1, 21);

      assert 28 == highestIdLessThanEqualTo(list1, 22);
      assert 28 == highestIdLessThanEqualTo(list1, 28);

      assert 28 == highestIdLessThanEqualTo(list1, 29);
   }

   private static void m2() {
      System.out.println("START");
      List<Integer> list1 = Arrays.asList(9, 9, 9, 14, 20);
      assert 9 == highestIdLessThanEqualTo(list1, 0);
      assert 9 == highestIdLessThanEqualTo(list1, 5);
      assert 9 == highestIdLessThanEqualTo(list1, 9);

      assert 14 == highestIdLessThanEqualTo(list1, 10);
      assert 14 == highestIdLessThanEqualTo(list1, 14);

      assert 20 == highestIdLessThanEqualTo(list1, 15);
      assert 20 == highestIdLessThanEqualTo(list1, 16);
      assert 20 == highestIdLessThanEqualTo(list1, 19);
      assert 20 == highestIdLessThanEqualTo(list1, 20);

      assert 20 == highestIdLessThanEqualTo(list1, 21);
      assert 20 == highestIdLessThanEqualTo(list1, 25);
   }

   private static void m1() {
      System.out.println("START");
      List<Integer> list1 = Arrays.asList(4, 4, 9, 9, 18);
      assert 4 == highestIdLessThanEqualTo(list1, 4);
      assert 4 == highestIdLessThanEqualTo(list1, 3);
      assert 4 == highestIdLessThanEqualTo(list1, 2);
      assert 4 == highestIdLessThanEqualTo(list1, 1);
      assert 4 == highestIdLessThanEqualTo(list1, 0);

      assert 9 == highestIdLessThanEqualTo(list1, 5);
      assert 9 == highestIdLessThanEqualTo(list1, 6);
      assert 9 == highestIdLessThanEqualTo(list1, 7);
      assert 9 == highestIdLessThanEqualTo(list1, 8);
      assert 9 == highestIdLessThanEqualTo(list1, 9);

      assert 18 == highestIdLessThanEqualTo(list1, 10);
      assert 18 == highestIdLessThanEqualTo(list1, 18);

      assert 18 == highestIdLessThanEqualTo(list1, 19);
      assert 18 == highestIdLessThanEqualTo(list1, 100);
      System.out.println("STOP");
   }

   public static int highestIdLessThanEqualTo(List<Integer> fingerTable, int k) {
      int highestId = -1;

      List<Integer> sortedFingerTable = new ArrayList<>(fingerTable);
      Collections.sort(sortedFingerTable);

      if (k < sortedFingerTable.get(0)) {
         highestId = sortedFingerTable.get(0);
      } else if (k < fingerTable.get(0)) {
         highestId = fingerTable.get(0);
      } else {

         for (int i = 0; i < sortedFingerTable.size() - 1; i++) {
            int j = sortedFingerTable.get(i);
            int jPlusOne = sortedFingerTable.get(i + 1);

            if (j < k) {
               highestId = j;
               if (k < jPlusOne) {
                  break;
               }
            }
         }
      }
       
      System.out.println(highestId);
      return highestId;
   }

}
