package hei.school.digital.bank.curdOperations;

import java.util.List;

public interface CrudOperations<T , T_id>{

  T create(T entity);
  List<T> findAll();

  T findById(T_id id);

  T update(T entity);

  void deleteById(T_id id);

}
