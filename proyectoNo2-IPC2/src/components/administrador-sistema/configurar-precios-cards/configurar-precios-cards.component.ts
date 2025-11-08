import { Component, inject, Input } from '@angular/core';
import { ConfiguracionAnuncioDTO } from '../../../models/anuncios/configuracion-anuncio-dto';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-configurar-precios-cards',
  imports: [CommonModule],
  templateUrl: './configurar-precios-cards.component.html',
  styleUrl: './configurar-precios-cards.component.scss'
})
export class ConfigurarPreciosCardsComponent {


  @Input() config!: ConfiguracionAnuncioDTO;

  router = inject(Router);

  //Metodo que sirve para editar la configuracion
  editar() {

    console.log('Editar config:', this.config);
    this.router.navigateByUrl(`/menu-admin-sistema/costos/precios/editar/${this.config.codigo}`);

  }
}
