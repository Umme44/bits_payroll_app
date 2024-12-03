import { Routes } from '@angular/router';
import { ConfigCustomUpdateComponent } from './config-custom-update.component';

export const configCustomRoute: Routes = [
  {
    path: '',
    component: ConfigCustomUpdateComponent,
    data: {
      pageTitle: 'bitsHrPayrollApp.config.home.title',
    },
  },
];
