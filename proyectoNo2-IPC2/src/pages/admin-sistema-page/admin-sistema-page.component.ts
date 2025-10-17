import { Component } from '@angular/core';
import { HeaderAdminComponent } from '../../components/administrador-sistema/header-admin.component/header-admin.component';
import { FooterComponent } from '../../components/footer/footer.component';

@Component({
  selector: 'app-admin-sistema-page.component',
  imports: [HeaderAdminComponent,FooterComponent],
  templateUrl: './admin-sistema-page.component.html',
  styleUrl: './admin-sistema-page.component.scss'
})
export class AdminSistemaPageComponent {

}
