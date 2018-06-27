package fadergs.edu.br.sigavan;

public class PassageiroViewHolderObjeto {
    private String nome;
    private String presenca;

    public PassageiroViewHolderObjeto(){
    }

    public  PassageiroViewHolderObjeto(String nome, String presenca){
        this.nome = nome;
        this.presenca = presenca;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPresenca() {
        return presenca;
    }

    public void setPresenca(String presenca) {
        this.presenca = presenca;
    }
}
