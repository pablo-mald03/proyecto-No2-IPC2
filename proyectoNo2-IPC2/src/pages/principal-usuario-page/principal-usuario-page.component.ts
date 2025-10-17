import { Component } from '@angular/core';
import { HeaderComponent } from "../../components/header/header.component";
import { FooterComponent } from "../../components/footer/footer.component";
import { NgFor, NgIf } from '@angular/common';

@Component({
  selector: 'app-principal-usuario-page',
  imports: [HeaderComponent, FooterComponent, NgFor, NgIf],
  templateUrl: './principal-usuario-page.component.html',
  styleUrl: './principal-usuario-page.component.scss'
})
export class PrincipalUsuarioPageComponent {


  //Pendiente jugar con las peticiones hacia el back
  showAd1 = true;
  showAd2 = false;
  showAd3 = false;
  showAd4 = true;
  showAd5 = false;
  showAd6 = true;



}
