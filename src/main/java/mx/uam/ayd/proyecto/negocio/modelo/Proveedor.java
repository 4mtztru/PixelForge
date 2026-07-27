package mx.uam.ayd.proyecto.negocio.modelo;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

/**
 * Entidad que representa a un Proveedor de mercancía en el sistema.
 * Contiene información legal (Razón Social, RFC), contacto y sus órdenes de compra asociadas.
 */
@Entity
public class Proveedor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idProveedor;

	private String razonSocial;

	private String rfc;

	private String contacto;

	@OneToMany(targetEntity = OrdenCompra.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "proveedor")
	private List<OrdenCompra> ordenesCompra = new ArrayList<>();

	public long getIdProveedor() {
		return idProveedor;
	}

	public void setIdProveedor(long idProveedor) {
		this.idProveedor = idProveedor;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getContacto() {
		return contacto;
	}

	public void setContacto(String contacto) {
		this.contacto = contacto;
	}

	public List<OrdenCompra> getOrdenesCompra() {
		return ordenesCompra;
	}

	public void setOrdenesCompra(List<OrdenCompra> ordenesCompra) {
		this.ordenesCompra = ordenesCompra;
	}
}
