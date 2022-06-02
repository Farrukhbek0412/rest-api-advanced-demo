package com.epam.esm.repository.tag;

public interface TagQueries {
    String GET_ALL = "select t from TagEntity t";
    String DELETE_BY_ID = "delete from TagEntity where id = :id";
    String FIND_BY_NAME = "select t from TagEntity t where t.name = :name";
    String GET_MOST_USED_TAG_OF_USER = """
            select * from tag t where t.id in (select ct.tag_id from certificate_tag ct where ct.certificate_id in
                      (select o.certificate_id from orders o where o.user_id = :userId)
                             group by ct.tag_id having count(ct.tag_id) =
                             (select distinct count(ct.tag_id) from certificate_tag ct where ct.certificate_id in
                                      (select o.certificate_id from orders o where o.user_id = :userId)
                                             group by (ct.tag_id) order by count(ct.tag_id) desc limit 1))""";
}
