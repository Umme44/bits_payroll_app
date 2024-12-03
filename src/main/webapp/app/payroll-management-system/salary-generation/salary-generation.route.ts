import { Route } from '@angular/router';
import { SalaryGenerationComponent } from './salary-generation.component';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';

export const SALARY_GENERATION_ROUTE: Route = {
  path: 'salary-generation',
  component: SalaryGenerationComponent,
  data: {
    authorities: [],
    pageTitle: 'bitsHrPayrollApp.salaryGeneration.home.title',
  },
  canActivate: [UserRouteAccessService],
};
