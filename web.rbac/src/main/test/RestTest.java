import com.fasterxml.jackson.core.type.TypeReference;
import drama.painter.core.web.misc.User;
import drama.painter.core.web.utility.Caches;
import drama.painter.core.web.utility.Json;
import drama.painter.web.rbac.RbacApplication;
import drama.painter.web.rbac.service.intf.oa.IStaff;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = RbacApplication.class)
public class RestTest {
    @Autowired
    Caches caches;

    @Autowired
    IStaff staff;

    @Test
    public void test() {
        List<User> product = Caches.getHash("Staff", Arrays.asList("shui", "admin", "kf333"), new TypeReference<List<User>>() {}, staff::get);
        System.out.println(Json.toJsonString(product));
    }

    // 笛卡尔乘积
    @Test
    public void descartes() {
        List<String> list1 = new ArrayList();
        list1.add("a");
        list1.add("b");
        List<String> list2 = new ArrayList();
        list2.add("0");
        list2.add("1");
        list2.add("2");
        List<List<String>> dimValue = new ArrayList();
        dimValue.add(list1);
        dimValue.add(list2);

        // 递归实现笛卡尔积
        Solution s = new Solution();
        List<List<String>> res = s.descartes(dimValue);
        System.out.println("递归实现笛卡尔乘积: 共 " + res.size() + " 个结果");
        for (List<String> list : res) {
            for (String string : list) {
                System.out.print(string + " ");
            }
            System.out.println();
        }
    }
}

class Solution {

    public List<List<String>> descartes(List<List<String>> dimValue) {
        List<List<String>> res = new ArrayList<>();
        if (dimValue == null || dimValue.size() == 0) {
            return res;
        }

        backtrace(dimValue, 0, res, new ArrayList<>());
        return res;
    }

    /**
     * 递归回溯法求解
     *
     * @param dimValue 原始数据集合
     * @param index    当前执行的集合索引
     * @param result   结果集合
     * @param curList  当前的单个结果集
     */
    private void backtrace(List<List<String>> dimValue, int index,
                           List<List<String>> result, List<String> curList) {

        if (curList.size() == dimValue.size()) {
            result.add(new ArrayList<>(curList));
        } else {
            for (int j = 0; j < dimValue.get(index).size(); j++) {
                curList.add(dimValue.get(index).get(j));
                backtrace(dimValue, index + 1, result, curList);
                curList.remove(curList.size() - 1);
            }
        }
    }
}

