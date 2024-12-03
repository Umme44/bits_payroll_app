import { Routes } from '@angular/router';

import { ImportDataComponent } from './import-data.component';
import { ImportEmployeeImageComponent } from 'app/admin/import-data/import-employee-image.component';
import { ImportEmployeeImageUploadComponent } from 'app/admin/import-data/import-employee-image-upload.component';
import { ImportEmployeeImageUpdateComponent } from 'app/admin/import-data/import-employee-image-update.component';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';

export const IMPORT_DATA_ROUTE: Routes = [
  {
    path: 'import-data',
    component: ImportDataComponent,
    data: {
      authorities: [],
      pageTitle: 'import-data.title',
    },
    canActivate: [UserRouteAccessService],
  },

  {
    path: 'import-data/import-employee-image',
    component: ImportEmployeeImageComponent,
    data: {
      authorities: [],
      pageTitle: 'import-data.title',
    },
    canActivate: [UserRouteAccessService],
  },

  {
    path: 'import-data/import-employee-image/upload',
    component: ImportEmployeeImageUploadComponent,
    data: {
      authorities: [],
      pageTitle: 'import-data.title',
    },
    canActivate: [UserRouteAccessService],
  },

  {
    path: 'import-data/import-employee-image/upload/:id',
    component: ImportEmployeeImageUpdateComponent,
    data: {
      authorities: [],
      pageTitle: 'import-data.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
