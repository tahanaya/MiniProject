package com.miniproject.DAO;

import com.miniproject.ENTITY.Etudiant;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface EtudiantDAO extends GenericDAO<Etudiant>{

     Etudiant mapToEtudiant(ResultSet rs) throws SQLException;
    int countStudents();
    List<Etudiant> getStudentsWithoutModules();

}
