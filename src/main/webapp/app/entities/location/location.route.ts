import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap, mergeMap } from 'rxjs/operators';

import { ILocation, Location } from 'app/shared/model/location.model';
import { LocationService } from './location.service';
import { LocationComponent } from './location.component';
import { LocationDetailComponent } from './location-detail.component';
import { LocationUpdateComponent } from './location-update.component';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';

@Injectable({ providedIn: 'root' })
export class LocationResolve implements Resolve<ILocation> {
  constructor(private service: LocationService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILocation | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((location: HttpResponse<Location>) => {
          if (location.body) {
            return of(location.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}

export const locationRoute: Routes = [
  {
    path: '',
    component: LocationComponent,
    data: {
      pageTitle: 'bitsHrPayrollApp.location.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LocationDetailComponent,
    resolve: {
      location: LocationResolve,
    },
    data: {
      pageTitle: 'bitsHrPayrollApp.location.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LocationUpdateComponent,
    resolve: {
      location: LocationResolve,
    },
    data: {
      pageTitle: 'bitsHrPayrollApp.location.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LocationUpdateComponent,
    resolve: {
      location: LocationResolve,
    },
    data: {
      pageTitle: 'bitsHrPayrollApp.location.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
