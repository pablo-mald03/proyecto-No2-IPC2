import { Component, Input } from '@angular/core';
import { Anuncio } from '../../../models/anuncios/anuncio';
import { TipoAnuncioEnum } from '../../../models/anuncios/tipo-anuncios-enum';
import { DatePipe, NgClass, NgIf } from '@angular/common';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-cards-reporte-anuncios',
  imports: [NgClass, DatePipe, NgIf],
  templateUrl: './cards-reporte-anuncios.component.html',
  styleUrl: './cards-reporte-anuncios.component.scss'
})
export class CardsReporteAnunciosComponent {

  @Input() anuncio!: Anuncio;


  constructor(private sanitizer: DomSanitizer) { }


  //Metodo que sirve para colocar los videos si end dado caso existen y verificar que la url sea segura
  getVideoUrl(): SafeResourceUrl {
    if (!this.anuncio.url) return '';
    const autoplay = false; 
    const safeUrl = this.anuncio.url.replace('autoplay=1', `autoplay=${autoplay ? 1 : 0}`);
    return this.sanitizer.bypassSecurityTrustResourceUrl(safeUrl);
  }

  //Metodo que ayuda a saber el tipo de anuncio que es 
  getTipoAnuncio(codigo: number): string {
    switch (codigo) {

      case 1: return TipoAnuncioEnum.ANUNCIO_TEXTO;
      case 2: return TipoAnuncioEnum.IMAGEN_TEXTO;
      case 3: return TipoAnuncioEnum.VIDEO_TEXTO;
      default: return 'Desconocido';
    }


  }

}
