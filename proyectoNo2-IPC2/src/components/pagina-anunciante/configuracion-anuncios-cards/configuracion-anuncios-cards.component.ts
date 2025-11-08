import { Component, Input, input } from '@angular/core';
import { ConfiguracionAnuncioDTO } from '../../../models/anuncios/configuracion-anuncio-dto';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-configuracion-anuncios-cards',
  imports: [CommonModule],
  templateUrl: './configuracion-anuncios-cards.component.html',
  styleUrl: './configuracion-anuncios-cards.component.scss'
})
export class ConfiguracionAnunciosCardsComponent {

    @Input() config!: ConfiguracionAnuncioDTO;

}
