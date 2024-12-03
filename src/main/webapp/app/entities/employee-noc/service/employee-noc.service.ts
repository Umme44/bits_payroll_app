import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEmployeeNOC, NewEmployeeNOC } from '../employee-noc.model';

export type PartialUpdateEmployeeNOC = Partial<IEmployeeNOC> & Pick<IEmployeeNOC, 'id'>;

type RestOf<T extends IEmployeeNOC | NewEmployeeNOC> = Omit<
  T,
  'leaveStartDate' | 'leaveEndDate' | 'issueDate' | 'createdAt' | 'updatedAt' | 'generatedAt'
> & {
  leaveStartDate?: string | null;
  leaveEndDate?: string | null;
  issueDate?: string | null;
  createdAt?: string | null;
  updatedAt?: string | null;
  generatedAt?: string | null;
};

export type RestEmployeeNOC = RestOf<IEmployeeNOC>;

export type NewRestEmployeeNOC = RestOf<NewEmployeeNOC>;

export type PartialUpdateRestEmployeeNOC = RestOf<PartialUpdateEmployeeNOC>;

export type EntityResponseType = HttpResponse<IEmployeeNOC>;
export type EntityArrayResponseType = HttpResponse<IEmployeeNOC[]>;

@Injectable({ providedIn: 'root' })
export class EmployeeNOCService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employee-nocs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(employeeNOC: NewEmployeeNOC): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employeeNOC);
    return this.http
      .post<RestEmployeeNOC>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(employeeNOC: IEmployeeNOC): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employeeNOC);
    return this.http
      .put<RestEmployeeNOC>(`${this.resourceUrl}/${this.getEmployeeNOCIdentifier(employeeNOC)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(employeeNOC: PartialUpdateEmployeeNOC): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employeeNOC);
    return this.http
      .patch<RestEmployeeNOC>(`${this.resourceUrl}/${this.getEmployeeNOCIdentifier(employeeNOC)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestEmployeeNOC>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEmployeeNOC[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEmployeeNOCIdentifier(employeeNOC: Pick<IEmployeeNOC, 'id'>): number {
    return employeeNOC.id;
  }

  compareEmployeeNOC(o1: Pick<IEmployeeNOC, 'id'> | null, o2: Pick<IEmployeeNOC, 'id'> | null): boolean {
    return o1 && o2 ? this.getEmployeeNOCIdentifier(o1) === this.getEmployeeNOCIdentifier(o2) : o1 === o2;
  }

  addEmployeeNOCToCollectionIfMissing<Type extends Pick<IEmployeeNOC, 'id'>>(
    employeeNOCCollection: Type[],
    ...employeeNOCSToCheck: (Type | null | undefined)[]
  ): Type[] {
    const employeeNOCS: Type[] = employeeNOCSToCheck.filter(isPresent);
    if (employeeNOCS.length > 0) {
      const employeeNOCCollectionIdentifiers = employeeNOCCollection.map(
        employeeNOCItem => this.getEmployeeNOCIdentifier(employeeNOCItem)!
      );
      const employeeNOCSToAdd = employeeNOCS.filter(employeeNOCItem => {
        const employeeNOCIdentifier = this.getEmployeeNOCIdentifier(employeeNOCItem);
        if (employeeNOCCollectionIdentifiers.includes(employeeNOCIdentifier)) {
          return false;
        }
        employeeNOCCollectionIdentifiers.push(employeeNOCIdentifier);
        return true;
      });
      return [...employeeNOCSToAdd, ...employeeNOCCollection];
    }
    return employeeNOCCollection;
  }

  protected convertDateFromClient<T extends IEmployeeNOC | NewEmployeeNOC | PartialUpdateEmployeeNOC>(employeeNOC: T): RestOf<T> {
    return {
      ...employeeNOC,
      leaveStartDate: employeeNOC.leaveStartDate?.format(DATE_FORMAT) ?? null,
      leaveEndDate: employeeNOC.leaveEndDate?.format(DATE_FORMAT) ?? null,
      issueDate: employeeNOC.issueDate?.format(DATE_FORMAT) ?? null,
      createdAt: employeeNOC.createdAt?.toJSON() ?? null,
      updatedAt: employeeNOC.updatedAt?.toJSON() ?? null,
      generatedAt: employeeNOC.generatedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restEmployeeNOC: RestEmployeeNOC): IEmployeeNOC {
    return {
      ...restEmployeeNOC,
      leaveStartDate: restEmployeeNOC.leaveStartDate ? dayjs(restEmployeeNOC.leaveStartDate) : undefined,
      leaveEndDate: restEmployeeNOC.leaveEndDate ? dayjs(restEmployeeNOC.leaveEndDate) : undefined,
      issueDate: restEmployeeNOC.issueDate ? dayjs(restEmployeeNOC.issueDate) : undefined,
      createdAt: restEmployeeNOC.createdAt ? dayjs(restEmployeeNOC.createdAt) : undefined,
      updatedAt: restEmployeeNOC.updatedAt ? dayjs(restEmployeeNOC.updatedAt) : undefined,
      generatedAt: restEmployeeNOC.generatedAt ? dayjs(restEmployeeNOC.generatedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEmployeeNOC>): HttpResponse<IEmployeeNOC> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEmployeeNOC[]>): HttpResponse<IEmployeeNOC[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
