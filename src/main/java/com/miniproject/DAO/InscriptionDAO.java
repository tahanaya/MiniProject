package com.miniproject.DAO;

import com.miniproject.ENTITY.Inscription;

public interface InscriptionDAO extends GenericDAO<Inscription> {
    boolean isStudentInModule(int moduleId, int studentId);
}
