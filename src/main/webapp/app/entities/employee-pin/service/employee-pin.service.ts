import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEmployeePin, NewEmployeePin } from '../employee-pin.model';

export type PartialUpdateEmployeePin = Partial<IEmployeePin> & Pick<IEmployeePin, 'id'>;

type RestOf<T extends IEmployeePin | NewEmployeePin> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

export type RestEmployeePin = RestOf<IEmployeePin>;

export type NewRestEmployeePin = RestOf<NewEmployeePin>;

export type PartialUpdateRestEmployeePin = RestOf<PartialUpdateEmployeePin>;

export type EntityResponseType = HttpResponse<IEmployeePin>;
export type EntityArrayResponseType = HttpResponse<IEmployeePin[]>;

@Injectable({ providedIn: 'root' })
export class EmployeePinService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employee-mgt/employee-pins');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(employeePin: NewEmployeePin): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employeePin);
    return this.http
      .post<RestEmployeePin>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(employeePin: IEmployeePin): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employeePin);
    return this.http
      .put<RestEmployeePin>(`${this.resourceUrl}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(employeePin: PartialUpdateEmployeePin): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employeePin);
    return this.http
      .patch<RestEmployeePin>(`${this.resourceUrl}/${this.getEmployeePinIdentifier(employeePin)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestEmployeePin>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  findBtPin(pin: string): Observable<EntityResponseType> {
    return this.http
      .get<RestEmployeePin>(`${this.resourceUrl}/find-by-pin/${pin}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEmployeePin[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEmployeePinIdentifier(employeePin: Pick<IEmployeePin, 'id'>): number {
    return employeePin.id;
  }

  isPinUnique(req?: any): Observable<HttpResponse<boolean>> {
    const options = createRequestOption(req);
    return this.http.get<boolean>(this.resourceUrl + `/is-pin-unique/`, { params: options, observe: 'response' });
  }

  declineEmployeeOnboard(id: number): Observable<EntityResponseType> {
    return this.http.get<IEmployeePin>(`${this.resourceUrl}/decline-employee-onboard/${id}`, { observe: 'response' });
  }

  compareEmployeePin(o1: Pick<IEmployeePin, 'id'> | null, o2: Pick<IEmployeePin, 'id'> | null): boolean {
    return o1 && o2 ? this.getEmployeePinIdentifier(o1) === this.getEmployeePinIdentifier(o2) : o1 === o2;
  }

  addEmployeePinToCollectionIfMissing<Type extends Pick<IEmployeePin, 'id'>>(
    employeePinCollection: Type[],
    ...employeePinsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const employeePins: Type[] = employeePinsToCheck.filter(isPresent);
    if (employeePins.length > 0) {
      const employeePinCollectionIdentifiers = employeePinCollection.map(
        employeePinItem => this.getEmployeePinIdentifier(employeePinItem)!
      );
      const employeePinsToAdd = employeePins.filter(employeePinItem => {
        const employeePinIdentifier = this.getEmployeePinIdentifier(employeePinItem);
        if (employeePinCollectionIdentifiers.includes(employeePinIdentifier)) {
          return false;
        }
        employeePinCollectionIdentifiers.push(employeePinIdentifier);
        return true;
      });
      return [...employeePinsToAdd, ...employeePinCollection];
    }
    return employeePinCollection;
  }

  protected convertDateFromClient<T extends IEmployeePin | NewEmployeePin | PartialUpdateEmployeePin>(employeePin: T): RestOf<T> {
    return {
      ...employeePin,
      createdAt: employeePin.createdAt?.toJSON() ?? null,
      updatedAt: employeePin.updatedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restEmployeePin: RestEmployeePin): IEmployeePin {
    return {
      ...restEmployeePin,
      createdAt: restEmployeePin.createdAt ? dayjs(restEmployeePin.createdAt) : undefined,
      updatedAt: restEmployeePin.updatedAt ? dayjs(restEmployeePin.updatedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEmployeePin>): HttpResponse<IEmployeePin> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEmployeePin[]>): HttpResponse<IEmployeePin[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
