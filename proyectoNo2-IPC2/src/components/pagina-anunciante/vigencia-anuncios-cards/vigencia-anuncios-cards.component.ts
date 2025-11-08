import { Component, Input } from '@angular/core';
import { VigenciaAnuncio } from '../../../models/anuncios/vigencia-anuncio';

@Component({
  selector: 'app-vigencia-anuncios-cards',
  imports: [],
  templateUrl: './vigencia-anuncios-cards.component.html',
  styleUrl: './vigencia-anuncios-cards.component.scss'
})
export class VigenciaAnunciosCardsComponent {

   @Input() vigencia!: VigenciaAnuncio;

}
