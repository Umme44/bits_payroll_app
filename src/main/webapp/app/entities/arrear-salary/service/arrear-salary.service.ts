import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IArrearSalary, NewArrearSalary } from '../arrear-salary.model';

export type PartialUpdateArrearSalary = Partial<IArrearSalary> & Pick<IArrearSalary, 'id'>;

export type EntityResponseType = HttpResponse<IArrearSalary>;
export type EntityArrayResponseType = HttpResponse<IArrearSalary[]>;

@Injectable({ providedIn: 'root' })
export class ArrearSalaryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payroll-mgt/arrear-salaries');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(arrearSalary: NewArrearSalary): Observable<EntityResponseType> {
    return this.http.post<IArrearSalary>(this.resourceUrl, arrearSalary, { observe: 'response' });
  }

  update(arrearSalary: IArrearSalary): Observable<EntityResponseType> {
    return this.http.put<IArrearSalary>(`${this.resourceUrl}/${this.getArrearSalaryIdentifier(arrearSalary)}`, arrearSalary, {
      observe: 'response',
    });
  }

  partialUpdate(arrearSalary: PartialUpdateArrearSalary): Observable<EntityResponseType> {
    return this.http.patch<IArrearSalary>(`${this.resourceUrl}/${this.getArrearSalaryIdentifier(arrearSalary)}`, arrearSalary, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IArrearSalary>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IArrearSalary[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getArrearSalaryIdentifier(arrearSalary: Pick<IArrearSalary, 'id'>): number {
    return arrearSalary.id;
  }

  compareArrearSalary(o1: Pick<IArrearSalary, 'id'> | null, o2: Pick<IArrearSalary, 'id'> | null): boolean {
    return o1 && o2 ? this.getArrearSalaryIdentifier(o1) === this.getArrearSalaryIdentifier(o2) : o1 === o2;
  }

  addArrearSalaryToCollectionIfMissing<Type extends Pick<IArrearSalary, 'id'>>(
    arrearSalaryCollection: Type[],
    ...arrearSalariesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const arrearSalaries: Type[] = arrearSalariesToCheck.filter(isPresent);
    if (arrearSalaries.length > 0) {
      const arrearSalaryCollectionIdentifiers = arrearSalaryCollection.map(
        arrearSalaryItem => this.getArrearSalaryIdentifier(arrearSalaryItem)!
      );
      const arrearSalariesToAdd = arrearSalaries.filter(arrearSalaryItem => {
        const arrearSalaryIdentifier = this.getArrearSalaryIdentifier(arrearSalaryItem);
        if (arrearSalaryCollectionIdentifiers.includes(arrearSalaryIdentifier)) {
          return false;
        }
        arrearSalaryCollectionIdentifiers.push(arrearSalaryIdentifier);
        return true;
      });
      return [...arrearSalariesToAdd, ...arrearSalaryCollection];
    }
    return arrearSalaryCollection;
  }
}
