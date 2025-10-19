import { Component, Input } from '@angular/core';
import { SalaMasGustadaDTO } from '../../../models/reportes/sala-mas-comentada-dto';
import { CommonModule, NgFor } from '@angular/common';

@Component({
  selector: 'app-salas-gustadas-cards',
  imports: [NgFor,CommonModule],
  templateUrl: './salas-gustadas-cards.component.html',
  styleUrl: './salas-gustadas-cards.component.scss'
})
export class SalasGustadasCardsComponent {

  @Input() salasGustadas!: SalaMasGustadaDTO;
  
}
