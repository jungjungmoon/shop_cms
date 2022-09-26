package shop.manager.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberParam {

    /**
     * manager - list.html 에서 검색 기능 구현
     */
    String searchType;
    String searchValue;
    // 페이지 처리
    long pageIndex;
    long pageSize;

     /*
     0 , 10 -> pageIndex: 1
     10, 10 -> pageIndex: 2
     20, 10 -> pageIndex: 3
     30, 10 -> pageIndex: 4
      */

    public long getPageStart() {
        init();
        return (pageIndex - 1) * pageSize;
    }

    public long getPageEnd() {
        init();
        return pageSize;
    }

    public void init() {
        if (pageIndex < 1) {
            pageIndex = 1;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }
    }

    /**
     * 페이지 전체 갯수 처리 (숫자값 전체 출력하는 부분)
     */
    public String getQueryString() {
        init();

        StringBuilder sb = new StringBuilder();
        if (searchType != null && searchType.length() > 0){
            sb.append(String.format("searchType=%s", searchType));
        }

        if (searchValue != null && searchValue.length() > 0){
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("searchValue=%s", searchValue));
        }
        return sb.toString();
    }
}
