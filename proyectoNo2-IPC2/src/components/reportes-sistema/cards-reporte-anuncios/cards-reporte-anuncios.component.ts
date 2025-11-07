import { Component, Input } from '@angular/core';
import { Anuncio } from '../../../models/anuncios/anuncio';
import { TipoAnuncioEnum } from '../../../models/anuncios/tipo-anuncios-enum';
import { DatePipe, NgClass, NgIf } from '@angular/common';
import { DomSanitizer, SafeResourceUrl, SafeUrl } from '@angular/platform-browser';
import { ReporteAnuncioDTO } from '../../../models/reportes/reporte-anuncio-dto';

@Component({
  selector: 'app-cards-reporte-anuncios',
  imports: [NgClass, DatePipe, NgIf],
  templateUrl: './cards-reporte-anuncios.component.html',
  styleUrl: './cards-reporte-anuncios.component.scss'
})
export class CardsReporteAnunciosComponent {

  @Input() anuncio!: ReporteAnuncioDTO;


  constructor(private sanitizer: DomSanitizer) { }

  //Metodo que permite obtener la imagen de tipo base64
  getImagenBase64(): SafeUrl | null {
    if (!this.esImagen()) return null;
    if (!this.anuncio.foto) return null;

    const base64 = `data:image/png;base64,${this.anuncio.foto}`;
    return this.sanitizer.bypassSecurityTrustUrl(base64);
  }

  //Metodo que sirve para colocar los videos si end dado caso existen y verificar que la url sea segura
  getVideoUrl(): SafeResourceUrl {
    if (!this.esVideo()) return '';
    if (!this.anuncio.url) return '';


    let url = this.anuncio.url;

    if (url.includes("watch?v=")) {
      const videoId = url.split("watch?v=")[1].split("&")[0];
      url = `https://www.youtube-nocookie.com/embed/${videoId}`;
    } else {
      url = url.replace("youtube.com", "youtube-nocookie.com");
    }

    return this.sanitizer.bypassSecurityTrustResourceUrl(url);
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


  //Metodos que ayudan a saber que tipo de anuncio es
  esTexto(): boolean {
    return this.anuncio.codigoTipo === 1;
  }

  esImagen(): boolean {
    return this.anuncio.codigoTipo === 2;
  }

  esVideo(): boolean {
    return this.anuncio.codigoTipo === 3;
  }


}
