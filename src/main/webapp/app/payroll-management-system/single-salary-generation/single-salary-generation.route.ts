import { Route } from '@angular/router';
import { SingleSalaryGenerationComponent } from './single-salary-generation.component';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';

export const SINGLE_SALARY_GENERATION_ROUTE: Route = {
  path: 'single-salary-generation',
  component: SingleSalaryGenerationComponent,
  data: {
    authorities: [],
    pageTitle: 'bitsHrPayrollApp.singleSalaryGeneration.home.title',
  },
  canActivate: [UserRouteAccessService],
};
