import { Component, Input } from '@angular/core';
import { AnunciosCompradosDTO } from '../../../models/reportes/anuncios-comprados-dto';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-ganacias-anuncios-ccomprados-cards',
  imports: [CommonModule],
  templateUrl: './ganacias-anuncios-ccomprados-cards.component.html',
  styleUrl: './ganacias-anuncios-ccomprados-cards.component.scss'
})
export class GanaciasAnunciosCcompradosCardsComponent {

  @Input() anuncioComprado!: AnunciosCompradosDTO;
}
