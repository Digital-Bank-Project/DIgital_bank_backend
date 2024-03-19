package hei.school.digital.bank.curdOperations;

public interface CrudOperations<T , T_id>{

  T create(T entity);

  T findById(T_id id);

  T update(T_id id);

  void deleteById(T_id id);

}
