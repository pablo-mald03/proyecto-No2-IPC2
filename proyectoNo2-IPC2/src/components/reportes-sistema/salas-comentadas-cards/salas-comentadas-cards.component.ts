import { Component, Input } from '@angular/core';
import { SalaMasComentadaDTO } from '../../../models/reportes/sala-mas-comentada-dto';
import { CommonModule, DatePipe } from '@angular/common';

@Component({
  selector: 'app-salas-comentadas-cards',
  imports: [DatePipe,CommonModule],
  templateUrl: './salas-comentadas-cards.component.html',
  styleUrl: './salas-comentadas-cards.component.scss'
})
export class SalasComentadasCardsComponent {

  @Input() salaComentada!: SalaMasComentadaDTO

}
