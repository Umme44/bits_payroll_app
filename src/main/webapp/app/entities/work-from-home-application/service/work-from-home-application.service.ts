import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWorkFromHomeApplication, NewWorkFromHomeApplication } from '../work-from-home-application.model';

export type PartialUpdateWorkFromHomeApplication = Partial<IWorkFromHomeApplication> & Pick<IWorkFromHomeApplication, 'id'>;

type RestOf<T extends IWorkFromHomeApplication | NewWorkFromHomeApplication> = Omit<
  T,
  'startDate' | 'endDate' | 'appliedAt' | 'updatedAt' | 'createdAt' | 'sanctionedAt'
> & {
  startDate?: string | null;
  endDate?: string | null;
  appliedAt?: string | null;
  updatedAt?: string | null;
  createdAt?: string | null;
  sanctionedAt?: string | null;
};

export type RestWorkFromHomeApplication = RestOf<IWorkFromHomeApplication>;

export type NewRestWorkFromHomeApplication = RestOf<NewWorkFromHomeApplication>;

export type PartialUpdateRestWorkFromHomeApplication = RestOf<PartialUpdateWorkFromHomeApplication>;

export type EntityResponseType = HttpResponse<IWorkFromHomeApplication>;
export type EntityArrayResponseType = HttpResponse<IWorkFromHomeApplication[]>;

@Injectable({ providedIn: 'root' })
export class WorkFromHomeApplicationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employee-mgt/work-from-home-applications');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(workFromHomeApplication: NewWorkFromHomeApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workFromHomeApplication);
    return this.http
      .post<RestWorkFromHomeApplication>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(workFromHomeApplication: IWorkFromHomeApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workFromHomeApplication);
    return this.http
      .put<RestWorkFromHomeApplication>(`${this.resourceUrl}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(workFromHomeApplication: PartialUpdateWorkFromHomeApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workFromHomeApplication);
    return this.http
      .patch<RestWorkFromHomeApplication>(
        `${this.resourceUrl}/${this.getWorkFromHomeApplicationIdentifier(workFromHomeApplication)}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestWorkFromHomeApplication>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  // query(req?: any): Observable<EntityArrayResponseType> {
  //   const options = createRequestOption(req);
  //   return this.http
  //     .get<RestWorkFromHomeApplication[]>(this.resourceUrl, { params: options, observe: 'response' })
  //     .pipe(map(res => this.convertResponseArrayFromServer(res)));
  // }

  query(req?: any): Observable<EntityArrayResponseType> {
    Object.keys(req).forEach(k => req[k] === null && delete req[k]);
    Object.keys(req).forEach(k => req[k] === undefined && delete req[k]);
    const options = createRequestOption(req);
    return this.http
      .get<RestWorkFromHomeApplication[]>(this.resourceUrl + '/by-date-ranges', { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  isAppliedForWorkFromHome(workFromHomeApplication: IWorkFromHomeApplication): Observable<HttpResponse<Boolean>> {
    const copy = this.convertDateFromClient(workFromHomeApplication);
    return this.http.post<Boolean>(`${this.resourceUrl}/isApplied`, copy, { observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getWorkFromHomeApplicationIdentifier(workFromHomeApplication: Pick<IWorkFromHomeApplication, 'id'>): number {
    return workFromHomeApplication.id;
  }

  compareWorkFromHomeApplication(
    o1: Pick<IWorkFromHomeApplication, 'id'> | null,
    o2: Pick<IWorkFromHomeApplication, 'id'> | null
  ): boolean {
    return o1 && o2 ? this.getWorkFromHomeApplicationIdentifier(o1) === this.getWorkFromHomeApplicationIdentifier(o2) : o1 === o2;
  }

  addWorkFromHomeApplicationToCollectionIfMissing<Type extends Pick<IWorkFromHomeApplication, 'id'>>(
    workFromHomeApplicationCollection: Type[],
    ...workFromHomeApplicationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const workFromHomeApplications: Type[] = workFromHomeApplicationsToCheck.filter(isPresent);
    if (workFromHomeApplications.length > 0) {
      const workFromHomeApplicationCollectionIdentifiers = workFromHomeApplicationCollection.map(
        workFromHomeApplicationItem => this.getWorkFromHomeApplicationIdentifier(workFromHomeApplicationItem)!
      );
      const workFromHomeApplicationsToAdd = workFromHomeApplications.filter(workFromHomeApplicationItem => {
        const workFromHomeApplicationIdentifier = this.getWorkFromHomeApplicationIdentifier(workFromHomeApplicationItem);
        if (workFromHomeApplicationCollectionIdentifiers.includes(workFromHomeApplicationIdentifier)) {
          return false;
        }
        workFromHomeApplicationCollectionIdentifiers.push(workFromHomeApplicationIdentifier);
        return true;
      });
      return [...workFromHomeApplicationsToAdd, ...workFromHomeApplicationCollection];
    }
    return workFromHomeApplicationCollection;
  }

  protected convertDateFromClient<T extends IWorkFromHomeApplication | NewWorkFromHomeApplication | PartialUpdateWorkFromHomeApplication>(
    workFromHomeApplication: T
  ): RestOf<T> {
    return {
      ...workFromHomeApplication,
      startDate: workFromHomeApplication.startDate?.format(DATE_FORMAT) ?? null,
      endDate: workFromHomeApplication.endDate?.format(DATE_FORMAT) ?? null,
      appliedAt: workFromHomeApplication.appliedAt?.format(DATE_FORMAT) ?? null,
      updatedAt: workFromHomeApplication.updatedAt?.toJSON() ?? null,
      createdAt: workFromHomeApplication.createdAt?.toJSON() ?? null,
      sanctionedAt: workFromHomeApplication.sanctionedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restWorkFromHomeApplication: RestWorkFromHomeApplication): IWorkFromHomeApplication {
    return {
      ...restWorkFromHomeApplication,
      startDate: restWorkFromHomeApplication.startDate ? dayjs(restWorkFromHomeApplication.startDate) : undefined,
      endDate: restWorkFromHomeApplication.endDate ? dayjs(restWorkFromHomeApplication.endDate) : undefined,
      appliedAt: restWorkFromHomeApplication.appliedAt ? dayjs(restWorkFromHomeApplication.appliedAt) : undefined,
      updatedAt: restWorkFromHomeApplication.updatedAt ? dayjs(restWorkFromHomeApplication.updatedAt) : undefined,
      createdAt: restWorkFromHomeApplication.createdAt ? dayjs(restWorkFromHomeApplication.createdAt) : undefined,
      sanctionedAt: restWorkFromHomeApplication.sanctionedAt ? dayjs(restWorkFromHomeApplication.sanctionedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestWorkFromHomeApplication>): HttpResponse<IWorkFromHomeApplication> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestWorkFromHomeApplication[]>): HttpResponse<IWorkFromHomeApplication[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
