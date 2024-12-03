import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IArrearSalaryMaster, NewArrearSalaryMaster } from '../arrear-salary-master.model';

export type PartialUpdateArrearSalaryMaster = Partial<IArrearSalaryMaster> & Pick<IArrearSalaryMaster, 'id'>;

export type EntityResponseType = HttpResponse<IArrearSalaryMaster>;
export type EntityArrayResponseType = HttpResponse<IArrearSalaryMaster[]>;

@Injectable({ providedIn: 'root' })
export class ArrearSalaryMasterService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/arrear-salary-masters');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(arrearSalaryMaster: NewArrearSalaryMaster): Observable<EntityResponseType> {
    return this.http.post<IArrearSalaryMaster>(this.resourceUrl, arrearSalaryMaster, { observe: 'response' });
  }

  update(arrearSalaryMaster: IArrearSalaryMaster): Observable<EntityResponseType> {
    return this.http.put<IArrearSalaryMaster>(
      `${this.resourceUrl}/${this.getArrearSalaryMasterIdentifier(arrearSalaryMaster)}`,
      arrearSalaryMaster,
      { observe: 'response' }
    );
  }

  partialUpdate(arrearSalaryMaster: PartialUpdateArrearSalaryMaster): Observable<EntityResponseType> {
    return this.http.patch<IArrearSalaryMaster>(
      `${this.resourceUrl}/${this.getArrearSalaryMasterIdentifier(arrearSalaryMaster)}`,
      arrearSalaryMaster,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IArrearSalaryMaster>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IArrearSalaryMaster[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getArrearSalaryMasterIdentifier(arrearSalaryMaster: Pick<IArrearSalaryMaster, 'id'>): number {
    return arrearSalaryMaster.id;
  }

  compareArrearSalaryMaster(o1: Pick<IArrearSalaryMaster, 'id'> | null, o2: Pick<IArrearSalaryMaster, 'id'> | null): boolean {
    return o1 && o2 ? this.getArrearSalaryMasterIdentifier(o1) === this.getArrearSalaryMasterIdentifier(o2) : o1 === o2;
  }

  addArrearSalaryMasterToCollectionIfMissing<Type extends Pick<IArrearSalaryMaster, 'id'>>(
    arrearSalaryMasterCollection: Type[],
    ...arrearSalaryMastersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const arrearSalaryMasters: Type[] = arrearSalaryMastersToCheck.filter(isPresent);
    if (arrearSalaryMasters.length > 0) {
      const arrearSalaryMasterCollectionIdentifiers = arrearSalaryMasterCollection.map(
        arrearSalaryMasterItem => this.getArrearSalaryMasterIdentifier(arrearSalaryMasterItem)!
      );
      const arrearSalaryMastersToAdd = arrearSalaryMasters.filter(arrearSalaryMasterItem => {
        const arrearSalaryMasterIdentifier = this.getArrearSalaryMasterIdentifier(arrearSalaryMasterItem);
        if (arrearSalaryMasterCollectionIdentifiers.includes(arrearSalaryMasterIdentifier)) {
          return false;
        }
        arrearSalaryMasterCollectionIdentifiers.push(arrearSalaryMasterIdentifier);
        return true;
      });
      return [...arrearSalaryMastersToAdd, ...arrearSalaryMasterCollection];
    }
    return arrearSalaryMasterCollection;
  }
}
