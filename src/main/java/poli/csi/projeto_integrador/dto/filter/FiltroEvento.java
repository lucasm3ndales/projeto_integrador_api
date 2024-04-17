package poli.csi.projeto_integrador.dto.filter;


public record FiltroEvento(
   String nome,
   String tipo,
   String periodicidade,
   String dataInicio,
   String dataFim,
   Boolean arquivado,
   String status
) {}
