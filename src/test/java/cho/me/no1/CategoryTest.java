package cho.me.no1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CategoryTest {
    private BoardCategoryManager manager;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        Category man = new Category(1L, "남자");
        Category woman = new Category(2L, "여자");
        Category exo = new Category(3L, "엑소");
        Category bts = new Category(4L, "방탄");
        Category blackpink = new Category(5L, "블랙핑크");

        List<Category> categories = List.of(man, woman, exo, bts, blackpink);

        Board notice1 = new Board(1L, "공지사항");
        Board chen = new Board(2L, "첸");
        Board bakhyeon = new Board(3L, "백현");
        Board xiumin = new Board(4L, "시우민");
        Board notice2 = new Board(5L, "공지사항");
        Board anonymous = new Board(6L, "익명게시판");
        Board v = new Board(7L, "뷔");
        Board notice3 = new Board(8L, "공지사항");
        Board rose = new Board(9L, "로제");

        List<Board> boards = List.of(notice1, chen, bakhyeon, xiumin, notice2, anonymous, v, notice3, rose);

        BoardCategoryRelation exoIsMan = new BoardCategoryRelation(1L, 3L);
        BoardCategoryRelation btsIsMan = new BoardCategoryRelation(1L, 4L);
        BoardCategoryRelation bpIsWoman = new BoardCategoryRelation(2L, 5L);

        BoardCategoryRelation exoNotice = new BoardCategoryRelation(1L, 1L);
        BoardCategoryRelation exoChen = new BoardCategoryRelation(1L, 2L);
        BoardCategoryRelation exoBakhyeon = new BoardCategoryRelation(1L, 3L);
        BoardCategoryRelation exoXiumin = new BoardCategoryRelation(1L, 4L);

        BoardCategoryRelation btsNotice = new BoardCategoryRelation(4L, 5L);
        BoardCategoryRelation btsAnonymous = new BoardCategoryRelation(4L, 6L);
        BoardCategoryRelation btsV = new BoardCategoryRelation(4L, 7L);

        BoardCategoryRelation bpNotice = new BoardCategoryRelation(5L, 8L);
        BoardCategoryRelation bpAnonymous = new BoardCategoryRelation(5L, 6L);
        BoardCategoryRelation bpRose = new BoardCategoryRelation(5L, 9L);

        List<BoardCategoryRelation> categoriesRelations = List.of(exoIsMan, btsIsMan, bpIsWoman);
        List<BoardCategoryRelation> boardRelations = List.of(exoNotice, exoChen, exoBakhyeon, exoXiumin, btsNotice, btsAnonymous, btsV, bpNotice, bpAnonymous, bpRose);

        manager = BoardCategoryManager.of(categories, boards, categoriesRelations, boardRelations);
    }

    @DisplayName("카테고리 식별자 '1L' 로 검색할 수 있습니다.")
    @Test
    void searchByCategoryId() throws JsonProcessingException {
        BoardCategoryJson boardCategoryJson = manager.findCategoriesById(1L).get();
        String string = objectMapper.writeValueAsString(boardCategoryJson);
        System.out.println(string);
    }

    @DisplayName("게시글 식별자 '1L' 로 검색할 수 있습니다.")
    @Test
    void searchByBoardId() throws JsonProcessingException {
        BoardCategoryJson boardCategoryJson = manager.findBoardsById(1L).get();
        String string = objectMapper.writeValueAsString(boardCategoryJson);
        System.out.println(string);
    }

    @DisplayName("카테고리 이름 '방탄' 으로 검색할 수 있습니다.")
    @Test
    void searchByCategoryName() throws JsonProcessingException {
        BoardCategoryJson json = manager.findByName("방탄").get();
        String string = objectMapper.writeValueAsString(json);
        System.out.println(string);
    }

    @DisplayName("게시글 이름 '뷔' 으로 검색할 수 있습니다.")
    @Test
    void searchByBoardName() throws JsonProcessingException {
        BoardCategoryJson json = manager.findBoardByName("뷔").get();
        String string = objectMapper.writeValueAsString(json);
        System.out.println(string);
    }

    @DisplayName("검색된 결과 값은 해당 카테고리의 하위 카테고리를 모두 담고 있어야 합니다.")
    @Test
    void verifySubcategoriesInSearchResult() throws JsonProcessingException {
        BoardCategoryJson boardCategoryJson = manager.findByName("블랙핑크").get();
        String result = objectMapper.writeValueAsString(boardCategoryJson);
        BoardCategoryJson actual = objectMapper.readValue(result, BoardCategoryJson.class);

        assertThat(actual.name()).isEqualTo("블랙핑크");
        assertThat(actual.id()).isEqualTo(5);

        List<BoardCategoryJson> actualList = actual.list();

        List<String> actualNames = actualList.stream()
                .map(BoardCategoryJson::name)
                .toList();
        assertThat(actualNames).hasSameHashCodeAs(List.of("공지사항", "익명게시판", "로제"));

        List<Long> ids = actualList.stream()
                .map(BoardCategoryJson::id)
                .toList();
        assertThat(ids).hasSameHashCodeAs(List.of(8L, 6L, 9L));
    }
}