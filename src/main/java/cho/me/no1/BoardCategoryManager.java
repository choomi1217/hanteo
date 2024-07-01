package cho.me.no1;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record BoardCategoryManager(Map<Long, Category> categories,
                                   Map<Long, Board> boards,
                                   List<BoardCategoryRelation> categoryRelations,
                                   List<BoardCategoryRelation> boardRelations) {
    public static BoardCategoryManager of(List<Category> categories,
                                          List<Board> boards,
                                          List<BoardCategoryRelation> categoriesRelations,
                                          List<BoardCategoryRelation> boardRelations) {

        Map<Long, Category> categoriesMap = categories.stream()
                .collect(Collectors.toMap(Category::id, category -> category));

        Map<Long, Board> boardsMap = boards.stream()
                .collect(Collectors.toMap(Board::id, board -> board));

        return new BoardCategoryManager(categoriesMap, boardsMap, categoriesRelations, boardRelations);
    }

    public Optional<BoardCategoryJson> findCategoriesById(long id) {
        Category category = categories.get(id);
        if (category == null) {
            return Optional.empty();
        }
        List<BoardCategoryJson> children = findChildren(category.id());
        List<BoardCategoryJson> boards = findBoards(category.id());

        return Optional.of(new BoardCategoryJson(category.name(), category.id(),
                Stream.concat(children.stream(), boards.stream()).toList()));
    }

    private List<BoardCategoryJson> findBoards(Long id) {
        return boardRelations.stream()
                .filter(relation -> relation.parentIdx().equals(id))
                .map(BoardCategoryRelation::childId)
                .filter(boards::containsKey)
                .map(boards::get)
                .map(board -> new BoardCategoryJson(board.name(), board.id(), Collections.emptyList()))
                .toList();
    }

    private List<BoardCategoryJson> findChildren(Long id) {
        return categoryRelations.stream()
                .filter(relation -> relation.parentIdx().equals(id))
                .map(relation -> findCategoriesById(relation.childId()))
                .filter(Objects::nonNull)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }


    public Optional<BoardCategoryJson> findByName(String name) {
        return categories.values()
                .stream()
                .filter(category -> category.name().equals(name))
                .findAny()
                .flatMap(category -> findCategoriesById(category.id()));
    }

    public Optional<BoardCategoryJson> findBoardsById(long id) {
        Board board = boards.get(id);
        return Optional.of(new BoardCategoryJson(board.name(), board.id(), Collections.emptyList()));
    }

    public Optional<BoardCategoryJson> findBoardByName(String name) {
        Board board = boards.values().stream().filter(findBoard -> findBoard.name().equals(name)).findFirst().get();
        return Optional.of(new BoardCategoryJson(board.name(), board.id(), Collections.emptyList()));
    }
}
