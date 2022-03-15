package life.majiang.community.dto;

import life.majiang.community.model.Question;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO<T> {
    private List<T> data;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer page;
    private List<Integer> pages = new ArrayList<>();
    private Integer endPage;

    public void setPagination(Integer totalCount, Integer page, Integer size) {
        this.page = page;

        Integer totalPage ;   //总页数
        if (totalCount % size == 0) {    //对总页数初始化
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        this.endPage = totalPage;
        pages.add(page);
        for (int i = 1; i <= 3 ; i++) {     //当前页码前后最多有三个页码，
            if(page - i > 0){
                pages.add(0, page - i);
            }
            if(page + i <= totalPage){
                pages.add(page + i);
            }
        }



        //是否展示上一页
        if (page == 1) {
            showPrevious = false;
        } else {
            showPrevious = true ;
        }


        //是否展示下一页
        if (page == totalPage) {
            showNext = false;
        } else {
            showNext = true;
        }
        //是否展示第一页
        if (pages.contains(1)) {
            showFirstPage = false;
        } else {
            showFirstPage = true;
        }
        //是否展示最后一页
        if (pages . contains(totalPage)) {
            showEndPage = false;
        } else {
            showEndPage = true;
        }


    }
}
