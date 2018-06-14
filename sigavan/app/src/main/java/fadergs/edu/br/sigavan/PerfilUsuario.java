package fadergs.edu.br.sigavan;

public class PerfilUsuario {
    private String nome;
    private String telefone;
    private String modoDeUso;

    public PerfilUsuario(){
    }

    public PerfilUsuario(String nome, String telefone, String modoDeUso){
        this.nome = nome;
        this.telefone = telefone;
        this.modoDeUso = modoDeUso;
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




}

