package cho.me.no1;

import java.util.List;

public record BoardCategoryJson(String name, Long id, List<BoardCategoryJson> list) {
}
