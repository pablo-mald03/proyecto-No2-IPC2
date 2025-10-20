import { Component, Input } from '@angular/core';
import { GananciasSistemaDTO } from '../../../models/reportes/ganancias-sistema-dto';
import { CommonModule } from '@angular/common';
import { GanaciasCostosCineCardsComponent } from "../ganacias-costos-cine-cards/ganacias-costos-cine-cards.component";
import { CardsReporteAnunciosComponent } from "../cards-reporte-anuncios/cards-reporte-anuncios.component";
import { GanaciasAnunciosCcompradosCardsComponent } from "../ganacias-anuncios-ccomprados-cards/ganacias-anuncios-ccomprados-cards.component";

@Component({
  selector: 'app-reporte-ganacias-cards',
  imports: [CommonModule, GanaciasCostosCineCardsComponent, GanaciasAnunciosCcompradosCardsComponent],
  templateUrl: './reporte-ganacias-cards.component.html',
  styleUrl: './reporte-ganacias-cards.component.scss'
})
export class ReporteGanaciasCardsComponent {

  @Input() gananciaProcesada!: GananciasSistemaDTO;
}
