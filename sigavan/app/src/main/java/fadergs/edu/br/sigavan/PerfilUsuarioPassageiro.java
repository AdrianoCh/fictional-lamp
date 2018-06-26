package fadergs.edu.br.sigavan;

public class PerfilUsuarioPassageiro {

    private String uid;
    private String nome;
    private String telefone;
    private String modoDeUso;
    private String primeiroLogin;
    private String email;

    public PerfilUsuarioPassageiro() {
    }

    public PerfilUsuarioPassageiro(String nome, String telefone, String modoDeUso, String primeiroLogin, String email) {
        this.uid = uid;
        this.nome = nome;
        this.telefone = telefone;
        this.modoDeUso = modoDeUso;
        this.primeiroLogin = primeiroLogin;
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getModoDeUso() {
        return modoDeUso;
    }

    public void setModoDeUso(String modoDeUso) {
        this.modoDeUso = modoDeUso;
    }

    public String getPrimeiroLogin() {
        return primeiroLogin;
    }

    public void setPrimeiroLogin(String primeiroLogin) {
        this.primeiroLogin = primeiroLogin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
