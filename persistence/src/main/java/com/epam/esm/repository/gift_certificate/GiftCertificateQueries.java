package com.epam.esm.repository.gift_certificate;

public interface GiftCertificateQueries {

    String GET_ALL = "select gc from GiftCertificateEntity gc";
    String DELETE = "delete from GiftCertificateEntity where id = :id";
    String UPDATE_DURATION = "update gift_certificate set duration = :duration,last_update_date= :time where id = :id";

    String UPDATE_PRICE = "update gift_certificate set price = :price,last_update_date= :time where id = :id";

    String GET_ALL_WITH_SEARCH
            = "select * from gift_certificate gc" +
              " where gc.name ilike '%'|| :searchWord || '%' or gc.description ilike '%'|| :searchWord || '%'";
    String SEARCH_WITH_MULTIPLE_TAGS
            = "select gc FROM GiftCertificateEntity gc " +
              "join gc.tagEntities t " +
              "where t in (:tags) " +
              "group by gc.id " +
              "having count(gc) = :tagCount";
    String GET_ALL_WITH_SEARCH_AND_TAG_NAME = """       
                        select * from gift_certificate gc join certificate_tag ct on gc.id = ct.certificate_id
                        where ct.tag_id = :tagId and
                        (gc.name ilike '%'|| :searchWord || '%' or gc.description ilike '%'|| :searchWord || '%')""";

    String ORDER_NAME_DATE_DESC = " order by gc.name desc, gc.create_date desc";
    String ORDER_NAME_DATE = " order by gc.name, gc.create_date";
    String ORDER_NAME_DESC = " order by gc.name desc";
    String ORDER_NAME = " order by gc.name";
    String ORDER_DATE_DESC = " order by gc.create_date desc";
    String ORDER_DATE = " order by gc.create_date";
    String NO_ORDER = "";


}
