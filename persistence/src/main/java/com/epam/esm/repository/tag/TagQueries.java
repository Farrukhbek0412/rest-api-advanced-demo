package com.epam.esm.repository.tag;

public interface TagQueries {
    String GET_ALL = "select t from TagEntity t";
    String DELETE_BY_ID = "delete from TagEntity where id = :id";
    String FIND_BY_NAME = "select t from TagEntity t where t.name = :name";
    String GET_MOST_USED_TAG_OF_USER = """
            SELECT * FROM tag  WHERE id in
                          (SELECT ct.tag_id FROM certificate_tag AS ct WHERE ct.certificate_id IN
                                      (SELECT ord.certificate_id FROM orders ord WHERE ord.user_id =
                                             (SELECT o.user_id FROM orders AS o
                                                GROUP BY o.user_id ORDER BY SUM(o.price) DESC limit 1))
                                                  GROUP BY ct.tag_id
                                                  HAVING count(ct.tag_id) = (SELECT count(cr.tag_id) FROM certificate_tag AS cr WHERE cr.certificate_id IN
                                                      (SELECT os.certificate_id FROM orders os WHERE os.user_id =
                                                        (SELECT o.user_id FROM orders AS o
                                                           GROUP BY o.user_id ORDER BY SUM(o.price) DESC limit 1))
                                                           GROUP BY cr.tag_id ORDER BY count(*) desc limit 1))
                           """;

    String COUNT = """
            SELECT count(ct.tag_id) FROM certificate_tag AS ct WHERE ct.certificate_id IN
                              (SELECT certificate_id FROM orders WHERE user_id =
                                     (SELECT uo.user_id FROM orders AS uo
                                           GROUP BY uo.user_id ORDER BY SUM(uo.price) DESC LIMIT 1))
                                     GROUP BY ct.tag_id ORDER BY count(*) desc limit 1
            """;

}
