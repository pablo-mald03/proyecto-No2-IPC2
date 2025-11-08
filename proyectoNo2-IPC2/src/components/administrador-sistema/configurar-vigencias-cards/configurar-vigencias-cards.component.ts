import { Component, inject, Input } from '@angular/core';
import { VigenciaAnuncio } from '../../../models/anuncios/vigencia-anuncio';
import { Router } from '@angular/router';

@Component({
  selector: 'app-configurar-vigencias-cards',
  imports: [],
  templateUrl: './configurar-vigencias-cards.component.html',
  styleUrl: './configurar-vigencias-cards.component.scss'
})
export class ConfigurarVigenciasCardsComponent {

  @Input() vigencia!: VigenciaAnuncio;

  router = inject(Router);

  //Metodo que sirve para editar la configuracion
  editar() {

    this.router.navigateByUrl(`/menu-admin-sistema/costos/tarifas/editar/${this.vigencia.codigo}`);

  }

}
