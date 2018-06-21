package fadergs.edu.br.sigavan;

public class PerfilUsuarioPassageiro {

    private String nome;
    private String telefone;
    private String modoDeUso;
    private Boolean primeiroLogin;
    private String email;
    private  String presenca;

    public PerfilUsuarioPassageiro(){
    }

    public PerfilUsuarioPassageiro(String nome, String telefone, String modoDeUso, Boolean primeiroLogin, String email, String presenca){
        this.nome = nome;
        this.telefone = telefone;
        this.modoDeUso = modoDeUso;
        this.primeiroLogin = primeiroLogin;
        this.email = email;
        this.presenca = presenca;
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

    public Boolean getPrimeiroLogin() {
        return primeiroLogin;
    }

    public void setPrimeiroLogin(Boolean primeiroLogin) {
        this.primeiroLogin = primeiroLogin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPresenca() {
        return presenca;
    }

    public  void setPresenca(){
        this.presenca = presenca;
    }
}
