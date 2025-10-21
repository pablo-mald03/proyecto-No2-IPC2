import { Component, Input } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ReporteSalaPeliculaProyectadaDTO } from '../../../models/reportes-cine/reporte-sala-pelicula-proyectada-dto';

@Component({
  selector: 'app-peliculas-proyectadas-cards',
  imports: [DatePipe],
  templateUrl: './peliculas-proyectadas-cards.component.html',
  styleUrl: './peliculas-proyectadas-cards.component.scss'
})
export class PeliculasProyectadasCardsComponent {

  @Input() salaPelicula!: ReporteSalaPeliculaProyectadaDTO;
}
