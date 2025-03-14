package model;

public class SquadMember {
    private int id;
    private String nome;
    private int tempoEmpresa;
    private String squad;
    private String funcao;

    public SquadMember(int id, String nome, int tempoEmpresa, String squad, String funcao) {
        this.id = id;
        this.nome = nome;
        this.tempoEmpresa = tempoEmpresa;
        this.squad = squad;
        this.funcao = funcao;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getNome() {

        return nome;
    }

    public void setNome(String nome) {

        this.nome = nome;
    }

    public int getTempoEmpresa() {

        return tempoEmpresa;
    }

    public void setTempoEmpresa(int tempoEmpresa) {

        this.tempoEmpresa = tempoEmpresa;
    }

    public String getSquad() {

        return squad;
    }

    public void setSquad(String squad) {

        this.squad = squad;
    }

    public String getFuncao() {

        return funcao;
    }

    public void setFuncao(String funcao) {

        this.funcao = funcao;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Nome: %s | Tempo: %d anos | Squad: %s | Função: %s", id, nome, tempoEmpresa, squad, funcao);
    }
}
