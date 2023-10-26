package isedPackage;

import java.sql.*;

public class ConnectionManager {
    private Connection conn;
    private Statement st;
    private ResultSet rsNames;
    private ResultSet rsDocuments;

    public ConnectionManager() throws Exception {
        conn = DriverManager.getConnection("jdbc:ucanaccess://src//main//resources//database//isedDatabase.accdb");
        st = conn.createStatement();
        rsNames = st.executeQuery("SELECT * from Names");
        rsDocuments = st.executeQuery("SELECT * from Documents");
    }

    public void closeConnection() throws Exception {
        st.close();
        conn.close();
    }

    public Statement getStatement() {
        return st;
    }

    public ResultSet getRSNames() {
        return rsNames;
    }

    public ResultSet getDocumentByName(String aName) throws Exception {
        ResultSet lResult = st.executeQuery("select * from Documents d join DocumentState s on s.id = d.status where DocumentName || ' (' || s.DocumentStateName || ')' = '" + aName + "'");
        lResult.next();
        return lResult;
    }

    public ResultSet getDocumentByID(String aID) throws Exception {
        ResultSet lResult = st.executeQuery("sElecT * fRoM DoCUMeNts wHeRe ID = '" + aID + "'");
        lResult.next();
        return lResult;
    }

    public ResultSet getUserByID(int aID) throws Exception {
        ResultSet lResult = st.executeQuery("sElecT * fRoM Names wHeRe ID = '" + aID + "'");
        lResult.next();
        return lResult;
    }

    public ResultSet getUserByName(String aName) throws Exception {
        ResultSet lResult = st.executeQuery("sElecT * fRoM Names wHeRe Login = '" + aName + "'");
        lResult.next();
        return lResult;
    }

    public ResultSet getAuthorByDocumentID(String aID) throws Exception {
        ResultSet lResult = st.executeQuery("SELECT Login FROM Names WHERE ID IN (SELECT NameID FROM Documents WHERE ID = '" + aID + "')");
        lResult.next();
        return lResult;
    }
}
