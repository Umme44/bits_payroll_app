import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { createRequestOption } from 'app/core/request/request-util';
import { ISalaryDeduction } from '../salary-deduction.model';

export type PartialUpdateSalaryDeduction = Partial<ISalaryDeduction> & Pick<ISalaryDeduction, 'id'>;

export type EntityResponseType = HttpResponse<ISalaryDeduction>;
export type EntityArrayResponseType = HttpResponse<ISalaryDeduction[]>;

@Injectable({ providedIn: 'root' })
export class SalaryDeductionService {
  public resourceUrl = SERVER_API_URL + 'api/payroll-mgt/salary-deductions';

  constructor(protected http: HttpClient) {}

  create(salaryDeduction: ISalaryDeduction): Observable<EntityResponseType> {
    return this.http.post<ISalaryDeduction>(this.resourceUrl, salaryDeduction, { observe: 'response' });
  }

  update(salaryDeduction: ISalaryDeduction): Observable<EntityResponseType> {
    return this.http.put<ISalaryDeduction>(this.resourceUrl, salaryDeduction, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISalaryDeduction>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISalaryDeduction[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  queryWithParams(year: number, month: number, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISalaryDeduction[]>(`${this.resourceUrl}/${year}/${month}`, {
      params: options,
      observe: 'response',
    });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSalaryDeductionIdentifier(salaryDeduction: Pick<ISalaryDeduction, 'id'>): number {
    return salaryDeduction.id;
  }

  compareSalaryDeduction(o1: Pick<ISalaryDeduction, 'id'> | null, o2: Pick<ISalaryDeduction, 'id'> | null): boolean {
    return o1 && o2 ? this.getSalaryDeductionIdentifier(o1) === this.getSalaryDeductionIdentifier(o2) : o1 === o2;
  }

  addSalaryDeductionToCollectionIfMissing<Type extends Pick<ISalaryDeduction, 'id'>>(
    salaryDeductionCollection: Type[],
    ...salaryDeductionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const salaryDeductions: Type[] = salaryDeductionsToCheck.filter(isPresent);
    if (salaryDeductions.length > 0) {
      const salaryDeductionCollectionIdentifiers = salaryDeductionCollection.map(
        salaryDeductionItem => this.getSalaryDeductionIdentifier(salaryDeductionItem)!
      );
      const salaryDeductionsToAdd = salaryDeductions.filter(salaryDeductionItem => {
        const salaryDeductionIdentifier = this.getSalaryDeductionIdentifier(salaryDeductionItem);
        if (salaryDeductionCollectionIdentifiers.includes(salaryDeductionIdentifier)) {
          return false;
        }
        salaryDeductionCollectionIdentifiers.push(salaryDeductionIdentifier);
        return true;
      });
      return [...salaryDeductionsToAdd, ...salaryDeductionCollection];
    }
    return salaryDeductionCollection;
  }
}
