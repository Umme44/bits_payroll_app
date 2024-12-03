import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { INomineeValidation } from '../../../shared/model/nominee-validation.model';
import Swal from 'sweetalert2';
import { INominee } from '../nominee.model';
import { IPfNominee } from '../pf-nominee.model';

type ResponseType = HttpResponse<INomineeValidation>;

@Injectable({ providedIn: 'root' })
export class NomineeValidationService {
  public resourceUrl = SERVER_API_URL + 'api/common';

  constructor(protected http: HttpClient) {}

  queryForPreValidateNominee(nominee: INominee): Observable<ResponseType> {
    return this.http.post<INomineeValidation>(this.resourceUrl + '/validate-nominee', nominee, { observe: 'response' });
  }
  queryForPreValidateNomineeForAdmin(nominee: INominee): Observable<ResponseType> {
    return this.http.post<INomineeValidation>(this.resourceUrl + '/validate-nominee-admin', nominee, { observe: 'response' });
  }

  queryForPreValidatePFNominee(nominee: IPfNominee): Observable<ResponseType> {
    return this.http.post<INomineeValidation>(this.resourceUrl + '/validate-pf-nominee', nominee, { observe: 'response' });
  }

  swalValidationMsg(msg: string): void {
    Swal.fire({
      icon: 'warning',
      text: msg,
      timer: 3000,
      showConfirmButton: false,
    });
  }
}
