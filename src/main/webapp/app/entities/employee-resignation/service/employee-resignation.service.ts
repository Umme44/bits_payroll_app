import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEmployeeResignation, NewEmployeeResignation } from '../employee-resignation.model';

export type PartialUpdateEmployeeResignation = Partial<IEmployeeResignation> & Pick<IEmployeeResignation, 'id'>;

type RestOf<T extends IEmployeeResignation | NewEmployeeResignation> = Omit<
  T,
  'createdAt' | 'updatedAt' | 'resignationDate' | 'lastWorkingDay'
> & {
  createdAt?: string | null;
  updatedAt?: string | null;
  resignationDate?: string | null;
  lastWorkingDay?: string | null;
};

export type RestEmployeeResignation = RestOf<IEmployeeResignation>;

export type NewRestEmployeeResignation = RestOf<NewEmployeeResignation>;

export type PartialUpdateRestEmployeeResignation = RestOf<PartialUpdateEmployeeResignation>;

export type EntityResponseType = HttpResponse<IEmployeeResignation>;
export type EntityArrayResponseType = HttpResponse<IEmployeeResignation[]>;

@Injectable({ providedIn: 'root' })
export class EmployeeResignationService {
/*   protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employee-resignations'); */
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employee-mgt/resignations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(employeeResignation: NewEmployeeResignation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employeeResignation);
    return this.http
      .post<RestEmployeeResignation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(employeeResignation: IEmployeeResignation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employeeResignation);
    return this.http
      .put<RestEmployeeResignation>(`${this.resourceUrl}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  approve(employeeResignation: IEmployeeResignation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employeeResignation);
    return this.http
      .put<RestEmployeeResignation>(`${this.resourceUrl}/approve`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  reject(employeeResignation: IEmployeeResignation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employeeResignation);
    return this.http
      .put<RestEmployeeResignation>(`${this.resourceUrl}/reject`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(employeeResignation: PartialUpdateEmployeeResignation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employeeResignation);
    return this.http
      .patch<RestEmployeeResignation>(`${this.resourceUrl}/${this.getEmployeeResignationIdentifier(employeeResignation)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestEmployeeResignation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEmployeeResignation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEmployeeResignationIdentifier(employeeResignation: Pick<IEmployeeResignation, 'id'>): number {
    return employeeResignation.id;
  }

  compareEmployeeResignation(o1: Pick<IEmployeeResignation, 'id'> | null, o2: Pick<IEmployeeResignation, 'id'> | null): boolean {
    return o1 && o2 ? this.getEmployeeResignationIdentifier(o1) === this.getEmployeeResignationIdentifier(o2) : o1 === o2;
  }

  addEmployeeResignationToCollectionIfMissing<Type extends Pick<IEmployeeResignation, 'id'>>(
    employeeResignationCollection: Type[],
    ...employeeResignationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const employeeResignations: Type[] = employeeResignationsToCheck.filter(isPresent);
    if (employeeResignations.length > 0) {
      const employeeResignationCollectionIdentifiers = employeeResignationCollection.map(
        employeeResignationItem => this.getEmployeeResignationIdentifier(employeeResignationItem)!
      );
      const employeeResignationsToAdd = employeeResignations.filter(employeeResignationItem => {
        const employeeResignationIdentifier = this.getEmployeeResignationIdentifier(employeeResignationItem);
        if (employeeResignationCollectionIdentifiers.includes(employeeResignationIdentifier)) {
          return false;
        }
        employeeResignationCollectionIdentifiers.push(employeeResignationIdentifier);
        return true;
      });
      return [...employeeResignationsToAdd, ...employeeResignationCollection];
    }
    return employeeResignationCollection;
  }

  protected convertDateFromClient<T extends IEmployeeResignation | NewEmployeeResignation | PartialUpdateEmployeeResignation>(
    employeeResignation: T
  ): RestOf<T> {
    return {
      ...employeeResignation,
      createdAt: employeeResignation.createdAt?.format(DATE_FORMAT) ?? null,
      updatedAt: employeeResignation.updatedAt?.format(DATE_FORMAT) ?? null,
      resignationDate: employeeResignation.resignationDate?.format(DATE_FORMAT) ?? null,
      lastWorkingDay: employeeResignation.lastWorkingDay?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restEmployeeResignation: RestEmployeeResignation): IEmployeeResignation {
    return {
      ...restEmployeeResignation,
      createdAt: restEmployeeResignation.createdAt ? dayjs(restEmployeeResignation.createdAt) : undefined,
      updatedAt: restEmployeeResignation.updatedAt ? dayjs(restEmployeeResignation.updatedAt) : undefined,
      resignationDate: restEmployeeResignation.resignationDate ? dayjs(restEmployeeResignation.resignationDate) : undefined,
      lastWorkingDay: restEmployeeResignation.lastWorkingDay ? dayjs(restEmployeeResignation.lastWorkingDay) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEmployeeResignation>): HttpResponse<IEmployeeResignation> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEmployeeResignation[]>): HttpResponse<IEmployeeResignation[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }

  getEmployeeNoticePeriod(employeeId: number): Observable<number> {
    return this.http.get<number>(`${this.resourceUrl}/notice-period/${employeeId}`);
  }

/*   reject(employeeResignation: IEmployeeResignation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employeeResignation);
    return this.http
      .put<IEmployeeResignation>(`${this.resourceUrl}/reject`, copy, { observe: 'response' })
      .pipe(map((res: RestEmployeeResignation) => this.convertResponseFromServer(res)));
  } */

/*   approve(employeeResignation: IEmployeeResignation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employeeResignation);
    return this.http
      .put<RestEmployeeResignation>(`${this.resourceUrl}/approve`, copy, { observe: 'response' })
      .pipe(map(res => this.convertDateFromServer(res)));
  } */

/*   reject(employeeResignation: IEmployeeResignation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employeeResignation);
    return this.http
      .put<IEmployeeResignation>(`${this.resourceUrl}/reject`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  } */
}
